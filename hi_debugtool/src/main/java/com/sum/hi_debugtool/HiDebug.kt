package com.sum.hi_debugtool

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/07 22:59
 * @类描述 ${TODO}
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)//只作用于方法
annotation class HiDebug(val name: String, val desc: String) {
}