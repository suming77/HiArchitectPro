package com.sum.hi.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatButton

/**
 * @author smy
 * @date 2022/7/22
 * @desc
 */
internal class IconFontButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attributeSet, defStyleAttr) {

    init {
        try {
            val typeface = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
            setTypeface(typeface)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("IconFontButton", e.toString() + e.message)
        }

    }
}