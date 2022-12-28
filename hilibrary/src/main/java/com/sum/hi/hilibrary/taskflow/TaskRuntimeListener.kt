package com.sum.hi.hilibrary.taskflow

import android.os.Build
import android.util.Log
import com.sum.hi.hilibrary.BuildConfig
import java.lang.StringBuilder

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/04 22:53
 * @类描述 ${TODO}
 */
class TaskRuntimeListener : TaskListener {
    override fun onStart(task: Task) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, task.id + START_METHOD)
        }
    }

    override fun onRunning(task: Task) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, task.id + RUNNING_METHOD)
        }
    }

    override fun onFinished(task: Task) {
        logTaskRuntimeInfo(task)
    }

    private fun logTaskRuntimeInfo(task: Task) {
        val taskRuntimeInfo = TaskRunTime.getTaskRuntimeInfo(task.id) ?: return
        val startTime = taskRuntimeInfo.stateTime[TaskState.START]
        val runningTime = taskRuntimeInfo.stateTime[TaskState.RUNNING]
        val finishedTime = taskRuntimeInfo.stateTime[TaskState.FINISHED]

        val sb = StringBuilder()
        sb.append(WRAPPER)
        sb.append(TAG)
        sb.append(WRAPPER)

        sb.append(WRAPPER)
        sb.append(HALF_LINE)
        sb.append(if (task is Project) "Project" else "task ${task.id} $FINISHED_METHOD")
        sb.append(HALF_LINE)

        addTaskInfoLineInfo(sb, DEPENDENCIES, getTaskDependenciesInfo(task))
        addTaskInfoLineInfo(sb, IS_BLOCK_TASK, taskRuntimeInfo.isBlockTask.toString())
        addTaskInfoLineInfo(sb, THREAD_NAME, taskRuntimeInfo.threadName!!)
        addTaskInfoLineInfo(sb, START_TIME, startTime.toString() + "ms")
        addTaskInfoLineInfo(sb, WAITING_TIME, (runningTime - startTime).toString() + "ms")
        addTaskInfoLineInfo(sb, TASK_CONSUME, (finishedTime - runningTime).toString() + "ms")
        addTaskInfoLineInfo(sb, FINISHED_TIME, (finishedTime).toString() + "ms")
        sb.append(HALF_LINE)
        sb.append(HALF_LINE)
        sb.append(WRAPPER)
        sb.append(WRAPPER)

        if (BuildConfig.DEBUG) {
            Log.e(TAG, sb.toString())
        }
    }

    private fun addTaskInfoLineInfo(
        sb: StringBuilder,
        key: String,
        value: String
    ) {
        sb.append("$key:$value")
    }

    private fun getTaskDependenciesInfo(task: Task): String {
        val build = StringBuilder()
        for (t in task.dependTaskName) {
            build.append(t)
        }
        return build.toString()
    }

    companion object {
        const val TAG: String = "TaskFlow"
        const val START_METHOD = "-- onStart --"
        const val RUNNING_METHOD = "-- onRunning --"
        const val FINISHED_METHOD = "-- onFinished --"

        const val DEPENDENCIES = "依赖任务"
        const val THREAD_NAME = "线程名称"
        const val START_TIME = "开始执行时间"
        const val WAITING_TIME = "等待执行耗时"
        const val TASK_CONSUME = "任务执行耗时"
        const val IS_BLOCK_TASK = "是否是阻塞任务"
        const val FINISHED_TIME = "任务结束时刻"
        const val WRAPPER = "\n"
        const val HALF_LINE = "==================================="
    }

}
