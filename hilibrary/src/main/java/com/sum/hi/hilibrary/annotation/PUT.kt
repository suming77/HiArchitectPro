package com.sum.hi.hilibrary.annotation

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/14 22:13
 * @类描述 ${TODO}
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PUT(val value: String, val formPost: Boolean = false)
