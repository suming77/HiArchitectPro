package com.sum.hi.hilibrary.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:04
 * @Desc:
 */
@Target(AnnotationTarget.FUNCTION)//作用于方法
@Retention(RetentionPolicy.RUNTIME)
annotation class GET(val value: String)