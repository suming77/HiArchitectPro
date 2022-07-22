package com.sum.hi.hilibrary.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

/**
 * @author smy
 * @date 2022/7/22
 * @desc
 */
object HiDateUtil {
    const val MM_FORMAT = "MM-dd"
    const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"
    fun getMMDate(date: Date): String {
        val sdf = SimpleDateFormat(MM_FORMAT, Locale.CHINA)
        return sdf.format(date)
    }

    fun getMMDate(dateStr: String): String {
        val sdf = SimpleDateFormat(DEFAULT_FORMAT, Locale.CHINA)
        return getMMDate(sdf.parse(dateStr)!!)
    }
}