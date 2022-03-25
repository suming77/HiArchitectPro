package com.sum.hi.hilibrary.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:17
 * @Desc:
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
annotation class POST(val value: String, val fromPost: Boolean = true)
