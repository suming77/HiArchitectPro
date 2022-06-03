package com.sum.hi.hilibrary.util

import android.app.Application

/**
 * @创建者 mingyan.su
 * @创建时间 2022/05/29 11:40
 * @类描述 ${TODO}
 */
object AppGlobals {
    val application: Application? = null

    //反射获取Application，避免初始化，调用invoke获得application实例
    fun get(): Application? {
        if (application == null) {
            try {
                Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return application
    }
}