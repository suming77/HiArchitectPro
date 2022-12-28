package com.sum.hi.hilibrary.taskflow

import androidx.annotation.IntDef

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/04 21:36
 * @类描述 ${TODO}
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(TaskState.IDLE, TaskState.START, TaskState.RUNNING, TaskState.FINISHED)
annotation class TaskState {
    companion object {
        const val IDLE = 0//静止
        const val START = 1//启动，可能需要等待调度
        const val RUNNING = 2//运行
        const val FINISHED = 3//运行结束
    }
}

