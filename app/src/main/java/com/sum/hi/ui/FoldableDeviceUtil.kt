package com.sum.hi.ui

import android.os.Build
import android.text.TextUtils
import com.sum.hi.hilibrary.util.AppGlobals
import com.sum.hi.hilibrary.util.HiDisplayUtil

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/31 17:29
 * @类描述 ${TODO}
 */
object FoldableDeviceUtil {
    //官方没有提供API，只能针对机型去检测
    val applicaition = AppGlobals.get()!!

    /**
     * 判断厂商然后判断机型型号 宽度是否等于折叠屏展开的最大宽度
     */
    fun fold(): Boolean {
        return if (TextUtils.equals(Build.BOARD, "samsung") && TextUtils.equals(
                Build.DEVICE,
                "Galaxy Z Fold2"
            )
        ) {
            return HiDisplayUtil.getDisplayWidthInPx(applicaition) != 1768
        } else if (TextUtils.equals(Build.BOARD, "huawei") && TextUtils.equals(
                Build.DEVICE,
                "MateX"
            )
        ) {
            return HiDisplayUtil.getDisplayWidthInPx(applicaition) != 2200
        } else if (TextUtils.equals(Build.BOARD, "google") && TextUtils.equals(
                Build.DEVICE,
                "generic_x86"
            )
        ) {//模拟器
            return HiDisplayUtil.getDisplayWidthInPx(applicaition) != 2200
        } else {
            true
        }
    }
}