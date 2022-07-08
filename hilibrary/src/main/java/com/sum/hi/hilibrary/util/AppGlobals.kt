package com.sum.hi.hilibrary.util

import android.app.Application

/**
 * @创建者 mingyan.su
 * @创建时间 2022/05/29 11:40
 * @类描述 ${TODO}
 */
object AppGlobals {
    private var application: Application? = null
    fun get(): Application? {
        if (application == null) {
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return application
    }
}