package com.sum.hi.hilibrary.taskflow

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/04 22:43
 * @类描述 ${TODO}
 */
interface TaskListener {
    fun onStart(task: Task)
    fun onRunning(task: Task)
    fun onFinished(task: Task)
}