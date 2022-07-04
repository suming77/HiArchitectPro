package com.sum.hi.common.view

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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
    private var desc: TextView
    private var icon: TextView
    private var title: TextView
    private var mIvIcon: TextView? = null
    private var mTvContent: TextView? = null
    private var emptyAction: TextView? = null
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

        icon = findViewById(R.id.empty_icon)
        title = findViewById(R.id.empty_title)
        desc = findViewById(R.id.empty_text)
        mIvIcon = findViewById(R.id.iv_empty)
        mTvContent = findViewById(R.id.tv_content)
        emptyAction = findViewById(R.id.empty_tips)
    }

    open fun setIcon(@StringRes iconRes: Int) {
        mIvIcon!!.setText(iconRes)
    }

    open fun setBtnRefresh(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            mBtnRefresh?.visibility = View.GONE
        } else {
            mBtnRefresh?.visibility = View.VISIBLE
            mBtnRefresh?.text = text
            mBtnRefresh?.setOnClickListener(listener)
        }
    }


    fun setTitle(text: String) {
        desc.text = text
        desc.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
    }

    open fun setDesc(text: String) {
        mTvContent?.text = text
        mTvContent?.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
    }


    //参数可选
    @JvmOverloads
    fun setHelpAction(@StringRes actionId: Int = R.string.if_detail, listener: OnClickListener) {
        emptyAction?.setText(actionId)
        emptyAction?.setOnClickListener(listener)
        emptyAction?.visibility = if (actionId == -1) View.GONE else View.VISIBLE
    }


    fun setButton(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            mBtnRefresh?.visibility = View.GONE
        } else {
            mBtnRefresh?.visibility = View.VISIBLE
            mBtnRefresh?.text = text
            mBtnRefresh?.setOnClickListener(listener)
        }
    }
}