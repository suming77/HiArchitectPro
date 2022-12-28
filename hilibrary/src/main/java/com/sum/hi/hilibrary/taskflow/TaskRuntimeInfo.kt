package com.sum.hi.hilibrary.taskflow

import android.util.SparseArray

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/11 17:29
 * @类描述 ${TODO}用以记录 每一个task实例的运行时信息的封装
 */
data class TaskRuntimeInfo(val task: Task) {
    var stateTime = SparseArray<Long>()
    var isBlockTask = false
    var threadName: String? = null
    fun setStateTime(@TaskState state: Int, time: Long) {
        stateTime.put(state, time)
    }

    fun isSameTask(task: Task): Boolean {
        return task != null && this.task == task
    }
}