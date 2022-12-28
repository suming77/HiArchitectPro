package com.sum.hi.hilibrary.taskflow

import android.text.TextUtils
import android.util.Log
import com.sum.hi.hilibrary.BuildConfig
import com.sum.hi.hilibrary.executor.HiExecutor
import com.sum.hi.hilibrary.util.MainHandler
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.*
import kotlin.Comparator

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/11 16:42
 * @类描述 ${TODO}
 * TaskFlow运行时的任务调度器
 * 1.根据task的属性以不同的策略（线程，同步，延迟）调度任务
 * 2.校验 依赖是否存在环形依赖
 * 3.校验依赖树中是否存在taskId相同的任务
 * 4.统计所有task的运行时信息（线程，状态，开始执行时间，耗时时间，是否阻塞任务）用于log输出
 */
internal object TaskRunTime {
    //通过addBlockTask(String name)指定启动阶段，需要阻塞完成的任务。只有当blockTaskId当中的任务都执行完了
    //才会释放Application的阻塞，才会拉起launchActivity
    val blockTaskId: MutableList<String> = mutableListOf()

    //如果blockTaskId 集合中的任务还没完成，那么主线程中执行的任务，会被添加到waitingTasks集合里面去
    //目的是为了优先保证，阻塞的任务优先完成，进可能早的launchActivity
    val waitingTasks: MutableList<Task> = mutableListOf()

    val taskRunTimeInfos: MutableMap<String, TaskRuntimeInfo> = HashMap()

    val taskComparator = Comparator<Task> { task1, task2 ->
        TaskUtil.compareTask(task1, task2)
    }

    @JvmStatic
    fun addBlockTask(id: String) {
        if (!TextUtils.isEmpty(id)) {
            blockTaskId.add(id)
        }
    }

    @JvmStatic
    fun addBlockTasks(vararg ids: String) {
        if (ids.isNotEmpty()) {
            for (id in ids) {
                addBlockTasks(id)
            }
        }
    }

    @JvmStatic
    fun removeBlockTask(id: String) {
        blockTaskId.remove(id)
    }

    @JvmStatic
    fun hasBlockTasks(): Boolean {
        return blockTaskId.iterator().hasNext()
    }

    @JvmStatic
    fun setThreadName(task: Task, threadName: String) {
        val taskRuntimeInfo = getTaskRuntimeInfo(task.id)
        taskRuntimeInfo?.threadName = threadName
    }

    @JvmStatic
    fun setStateInfo(task: Task) {
        val taskRuntimeInfo = getTaskRuntimeInfo(task.id)
        taskRuntimeInfo?.setStateTime(task.state, System.currentTimeMillis())
    }

    @JvmStatic
    fun getTaskRuntimeInfo(id: String): TaskRuntimeInfo? {
        return taskRunTimeInfos[id]
    }

    //根据task的属性以不同的策略 调度task
    @JvmStatic
    fun executeTask(task: Task) {
        if (task.isAsyncTask) {
            HiExecutor.execute(runnable = task)
        } else {
            //else 里面的都是在主线程去执行
            //延迟任务，但是如果这个延迟任务，它存在着后置任务 A(延迟任务) -> B ->C（BlockTask）
            if (task.delayMills > 0 && !hasBlockBehindTask(task)) {
                MainHandler.postDelay(task.delayMills, task)
                return
            }

            if (!hasBlockTasks()) {
                task.run()
            } else {
                addWaitingTask(task)
            }
        }
    }

    //把一个主线程上需要执行的任务，但又不影响launchActivity启动，添加到等待队列
    private fun addWaitingTask(task: Task) {
        if (!waitingTasks.contains(task)) {
            waitingTasks.add(task)
        }
    }

    //检测一个延迟任务，是否存在后置的阻塞任务，（就是等他们都在执行完了，才会释放application的阻塞，才会拉起launchActivity）
    private fun hasBlockBehindTask(task: Task): Boolean {
        if (task is Project.CriticalTask) {
            return false
        }
        val behindTasks = task.behindTasks
        for (behindTask in behindTasks) {
            //需要判断一个task是不是阻塞任务，blockTaskIds
            val behindTaskInfo = getTaskRuntimeInfo(task.id)
            return if (behindTaskInfo != null && behindTaskInfo.isBlockTask) {
                true
            } else {
                hasBlockBehindTask(behindTask)
            }
        }
        return false
    }


    //校验 依赖树中是否存在环形依赖校验，依赖树中是否存在taskId相同的任务，初始化task对应的taskRunTimeInfo
    //遍历依赖树 完成启动前的检查 和初始化
    @JvmStatic
    fun traversalDependencyTreeAndInit(task: Task) {
        val traversalVisitor = linkedSetOf<Task>()
        traversalVisitor.add(task)
        innerTraversalDependencyTreeAndInit(task, traversalVisitor)

        val iterator = blockTaskId.iterator()
        while (iterator.hasNext()) {
            val taskId = iterator.next()
            //检查这个阻塞任务，是否存在依赖树中
            if (!taskRunTimeInfos.containsKey(taskId)) {
                throw RuntimeException("block task ${task.id} not in dependency tree.")
            } else {
                val task = getTaskRuntimeInfo(taskId)?.task
                traversalDependencyPriority(task)
            }
        }
    }

    private fun traversalDependencyPriority(task: Task?) {
        if (task == null) {
            return
        }
    }

    private fun innerTraversalDependencyTreeAndInit(
        task: Task,
        traversalVisitor: LinkedHashSet<Task>
    ) {
        //初始化taskRuntimeInfo，并且校验是否存在相同的任务名称id
        var taskRuntimeInfo = getTaskRuntimeInfo(task.id)
        if (taskRuntimeInfo == null) {
            taskRuntimeInfo = TaskRuntimeInfo(task)
            if (blockTaskId.contains(task.id)) {
                taskRuntimeInfo.isBlockTask = true
            }
            taskRunTimeInfos[task.id] = taskRuntimeInfo
        } else {
            if (!taskRuntimeInfo.isSameTask(task)) {
                throw RuntimeException("not allow to contain same id ${task.id}")
            }
        }

        //校验环形依赖
        task.behindTasks.forEach {
            if (!traversalVisitor.contains(it)) {
                traversalVisitor.add(it)
            } else {
                throw RuntimeException("not allow loopback dependency, task id ${it.id}")
            }

            //start->task1->task2->task3->task4->task5->end
            //对task3后面的依赖任务路径上的task做环形依赖检查初始化runTimeInfo信息
            if (BuildConfig.DEBUG && it.behindTasks.isEmpty()) {
                //behindTask = end
                val iterator = traversalVisitor.iterator()
                val sb = StringBuilder()
                while (iterator.hasNext()) {
                    sb.append(iterator.next().id)
                    sb.append(" -->")
                }

                Log.e(TaskRuntimeListener.TAG, sb.toString())
            }
            innerTraversalDependencyTreeAndInit(it, traversalVisitor)
            traversalVisitor.remove(it)
        }
    }

    @JvmStatic
    fun hasWaitingTasks(): Boolean {
        return waitingTasks.iterator().hasNext()
    }

    fun runWaitingTasks() {
        if (hasWaitingTasks()) {
            if (waitingTasks.size > 1) {
                Collections.sort(waitingTasks, taskComparator)
            }
            if (hasBlockTasks()) {
                val head = waitingTasks.removeAt(0)
                head.run()
            } else {
                for (waitTask in waitingTasks) {
                    MainHandler.postDelay(waitTask.delayMills, waitTask)
                }
                waitingTasks.clear()
            }


        }
    }

}