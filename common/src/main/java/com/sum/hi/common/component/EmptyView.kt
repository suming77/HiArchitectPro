package com.sum.hi.common.component

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.button.MaterialButton
import com.sum.hi.common.R

/**
 * @Author:   smy
 * @Date:     2022/2/19 15:52
 * @Desc:
 */
class EmptyView : LinearLayout {

    private var mIvIcon: TextView? = null
    private var mTvContent: TextView? = null
    private var mBtnRefresh: MaterialButton? = null

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0) {

    }

    constructor(context: Context, attributes: AttributeSet?, defaultStyle: Int) : super(
        context,
        attributes,
        defaultStyle
    ) {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)

        mIvIcon = findViewById(R.id.iv_empty)
        mTvContent = findViewById(R.id.tv_content)
        mBtnRefresh = findViewById(R.id.mb_refresh)

        var typeface1 = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
        mIvIcon!!.typeface = typeface1
    }

    fun setIcon(@StringRes iconRes: Int) {
        mIvIcon!!.setText(iconRes)
    }

    fun setContent(content: String) {
        mTvContent?.text = content
        mTvContent?.visibility = if (TextUtils.isDigitsOnly(content)) View.GONE else View.VISIBLE
    }

    fun setBtnRefresh(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            mBtnRefresh?.visibility = View.GONE
        } else {
            mBtnRefresh?.visibility = View.VISIBLE
            mBtnRefresh?.text = text
            mBtnRefresh?.setOnClickListener(listener)
        }
    }
}