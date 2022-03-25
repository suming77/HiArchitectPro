package com.sum.hi.hilibrary.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:11
 * @Desc:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(RetentionPolicy.RUNTIME)
annotation class Headers(vararg val value: String)