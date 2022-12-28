package com.sum.hi.hilibrary.taskflow

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/04 23:59
 * @类描述 ${TODO}
 */
object TaskUtil {
    //比较两个任务的优先级
    //优先级越高的越先执行
    fun compareTask(task1: Task, task2: Task): Int {
        return when {
            task1.priority > task2.priority -> {
                -1
            }
            task1.priority < task2.priority -> {
                1
            }
            else -> {
                0
            }
        }
    }
}