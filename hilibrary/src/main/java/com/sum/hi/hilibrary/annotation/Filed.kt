package com.sum.hi.hilibrary.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Author:   smy
 * @Date:     2022/3/23 20:57
 * @Desc:
 */
@Target(AnnotationTarget.VALUE_PARAMETER)//标记在一个参数上面的意思
@Retention(RetentionPolicy.RUNTIME)
annotation class Filed(val value: String)