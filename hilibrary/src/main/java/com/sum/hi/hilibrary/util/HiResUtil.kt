package com.sum.hi.hilibrary.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * @author smy
 * @date 2022/7/21
 * @desc
 */
object HiResUtil {
    private fun context(): Context {
        return AppGlobals.get() as Context
    }

    fun string(@StringRes id: Int): String {
        return context().getString(id)
    }

    fun color(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context(), id)
    }

    fun drawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context(), id)
    }

    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context(), id)
    }
}