package com.sum.hi.hilibrary.taskflow

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/05 23:27
 * @类描述 ${TODO}
 */
interface ITaskCreator {
    fun creatorTask(taskName: String): Task
}