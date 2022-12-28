package com.sum.hi.ui.aspectj

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/15 22:37
 * @类描述 ${TODO}
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class MethodTrace{}

