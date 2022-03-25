package com.sum.hi.hilibrary.annotation

/**
 * @Author:   smy
 * @Date:     2022/3/23 20:24
 * @Desc:
 */
@Target(AnnotationTarget.FUNCTION)//标记了一个方法上面
@Retention(AnnotationRetention.RUNTIME)//标记在运行时依旧能够保留
annotation class BaseUrl(val value: String)