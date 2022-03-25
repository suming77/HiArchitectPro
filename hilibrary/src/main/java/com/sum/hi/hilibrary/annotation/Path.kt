package com.sum.hi.hilibrary.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:14
 * @Desc:
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
annotation class Path(val value: String)
