package com.sum.hi.hilibrary.taskflow

import android.os.Looper
import androidx.annotation.MainThread

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/12 08:40
 * @类描述 ${TODO}
 * 对taskRuntime的包装，对外暴露的类，用于启动任务
 */
object TaskFlowManager {
    fun addBlockTask(taskId: String): TaskFlowManager {
        TaskRunTime.addBlockTask(taskId)
        return this
    }

    fun addBlockTask(vararg taskIds: String): TaskFlowManager {
        TaskRunTime.addBlockTasks(*taskIds)
        return this
    }

    //Project任务组，也可能是独立的一个task
    @MainThread
    fun start(task: Task) {
        assert(Thread.currentThread() == Looper.getMainLooper().thread) {
            ("start method must be invoke on MainThread")
        }

        val startTask = if (task is Project) task.startTask else task
        TaskRunTime.traversalDependencyTreeAndInit(startTask)
        startTask.start()
        while (TaskRunTime.hasBlockTasks()) {
            try {
                Thread.sleep(10)
            } catch (e: Exception) {

            }
            //主线程唤醒之后，存在着等待队列的任务
            //那么让等待任务中的任务执行
            while (TaskRunTime.hasBlockTasks()) {
                TaskRunTime.runWaitingTasks()
            }

        }
    }
}