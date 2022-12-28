package com.sum.hi.hilibrary.taskflow

import androidx.core.os.TraceCompat
import java.lang.RuntimeException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/04 21:22
 * @类描述 ${TODO}
 */
abstract class Task @JvmOverloads constructor(
    val id: String,//任务名称
    val isAsyncTask: Boolean = false,//是否是异步任务
    val delayMills: Long = 0,//延迟执行的时间
    var priority: Int = 0//任务的优先级
) : Runnable, Comparable<Task> {//实现Comparable接口，需要做优先级排序

    var executeTime: Long = 0
        //执行时间
        protected set
    var state: Int = TaskState.IDLE
        //任务的状态
        protected set

    //当前Task依赖了那些前置任务，只有当dependTasks集合中的所有任务执行完，当前才可以执行
    val dependTasks: MutableList<Task> = ArrayList()

    //当前Task被那些后置任务依赖，只有当当前Task执行完，behindTasks集合中的后置任务执才可以执行
    val behindTasks: MutableList<Task> = ArrayList()

    //依赖任务的名称， 方便打印数据
    val dependTaskName: MutableList<String> = ArrayList()

    //任务运行状态监听器集合
    private val taskListeners: MutableList<TaskListener> = ArrayList()
    private var taskRunningTimeListener: TaskRuntimeListener? =
        TaskRuntimeListener()//用于输出task 运行时的日志

    fun addTaskListener(listener: TaskListener) {
        if (!taskListeners.contains(listener)) {
            taskListeners.add(listener)
        }
    }

    open fun start() {
        if (state != TaskState.IDLE) {
            throw RuntimeException("cannot run task $id again")
        }
        toStart()
        executeTime = System.currentTimeMillis()
        //执行当前任务
        executeTask(this)
    }

    private fun executeTask(task: Task) {

    }

    private fun toStart() {
        state = TaskState.START
        TaskRunTime.setStateInfo(this)
        for (listener in taskListeners) {
            listener.onStart(this)
        }
        taskRunningTimeListener?.onStart(this)
    }

    override fun run() {
        TraceCompat.beginSection(id)
        //改变任务状态，onStart, onRunning，onFinished --通知后置任务开始去执行
        toRunning()
        run(id)//真正的执行 初始化代码的方法
        toFinished()
        //通知它的后置任务去执行
        notifyBehindTask()
        recycler()
        TraceCompat.endSection()
    }

    private fun recycler() {
        dependTasks.clear()
        behindTasks.clear()
        taskListeners.clear()
        taskRunningTimeListener = null
    }

    private fun notifyBehindTask() {
        //通知后置任务去尝试执行
        if (behindTasks.isNotEmpty()) {
            if (behindTasks.size > 1) {
                Collections.sort(behindTasks, TaskRunTime.taskComparator)
            }

            //遍历behindTasks后置任务，通知他们，告诉他们你的一个前置依赖任务已经执行完成了
            for (behindTask in behindTasks) {
                //A behindTask （B,C） A执行完成之后，B,C才可以执行
                behindTask.dependTaskFinish(this)
            }
        }
    }

    private fun dependTaskFinish(dependTask: Task) {
        //A behindTask （B,C） A执行完成之后，B,C才可以执行
        //task = B,C dependTask = A
        if (dependTasks.isEmpty()) {
            return
        }

        //把A从B,C的前置依赖任务集合中移除
        dependTasks.remove(dependTask)
        //B,C的所有前置任务，是否都执行完了
        if (dependTasks.isEmpty()) {
            start()
        }
    }

    //给当前task添加一个前置的依赖任务
    open fun dependOn(dependTask: Task) {
        var task = dependTask
        if (this != task) {
            if (dependTask is Project){
                task = dependTask.endTask
            }
            dependTasks.add(task)
            dependTaskName.add(task.id)

            //当前task依赖了dependTask，那么我么还需要把dependTask里面的behindTask添加进去当前Task
            if (!task.behindTasks.contains(this)) {
                task.behindTasks.add(this)
            }
        }
    }

    //给当前Task移除一个前置依赖任务
    open fun removeDependence(dependTask: Task) {
        var task = dependTask
        if (this != dependTask) {

            if (dependTask is Project){
                task = dependTask.endTask
            }
            dependTasks.remove(task)
            dependTaskName.remove(task.id)

            //把当前Task从dependTask的后置任务集合behindTasks中移除
            if (!task.behindTasks.contains(this)) {
                task.behindTasks.remove(this)
            }
        }
    }

    //给当前任务添加后置任务依赖项
    //他和dependOn是相反的
    open fun behind(behindTask: Task) {
        var task = behindTask
        if (this != behindTask) {
            if (behindTask is Project){
                task = behindTask.startTask
            }
            //这个是把behindTask添加到当前Task的后面
            behindTasks.add(task)

            //把当前task添加到behindTask的前面,这样才能建立双向关系
            behindTask.dependOn(this)
        }
    }

    //给当前Task移除一个后置的任务
    open fun removeBehind(behindTask: Task) {
        var task = behindTask
        if (this != behindTask) {
            if (behindTask is Project){
                task = behindTask.startTask
            }
            behindTasks.remove(task)
            //解除两个Task之间的依赖关系
            behindTask.removeDependence(this)
        }
    }

    private fun toFinished() {
        state = TaskState.FINISHED
        TaskRunTime.setStateInfo(this)
        for (listener in taskListeners) {
            listener.onFinished(this)
        }
        taskRunningTimeListener?.onFinished(this)
    }

    private fun toRunning() {
        state = TaskState.RUNNING
        TaskRunTime.setStateInfo(this)
        TaskRunTime.setThreadName(this, Thread.currentThread().name)
        for (listener in taskListeners) {
            listener.onRunning(this)
        }
        taskRunningTimeListener?.onRunning(this)
    }

    abstract fun run(id: String)

    override fun compareTo(other: Task): Int {
        return TaskUtil.compareTask(this, other)
    }
}