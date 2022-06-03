package com.sum.hi.hilibrary.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:17
 * @Desc:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( AnnotationTarget.FUNCTION)
annotation class POST(val value: String, val fromPost: Boolean = true)
