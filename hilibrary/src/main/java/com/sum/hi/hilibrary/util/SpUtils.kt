package com.sum.hi.hilibrary.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @创建者 mingyan.su
 * @创建时间 2022/05/29 11:53
 * @类描述 ${TODO}
 */
object SpUtils {
    val CACHE_FILE = "cache_file"
    fun getSharedPreferences(): SharedPreferences? {
        val application = AppGlobals.get()
        if (application != null) {
            return application.getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE)
        } else {
            return null
        }
    }

    fun putString(key: String, value: String) {
        val sp = getSharedPreferences()
        sp?.edit()?.putString(key, value)?.commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        val sp = getSharedPreferences()
        sp?.edit()?.putBoolean(key, value)?.commit()
    }

    fun putInt(key: String, value: Int) {
        val sp = getSharedPreferences()
        sp?.edit()?.putInt(key, value)?.commit()
    }

    fun getString(key: String): String? {
        val sp = getSharedPreferences()
        return sp?.getString(key, "")
    }

    fun getInt(key: String): Int? {
        val sp = getSharedPreferences()
        return sp?.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean? {
        val sp = getSharedPreferences()
        return sp?.getBoolean(key, false)
    }

}