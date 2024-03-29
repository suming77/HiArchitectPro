package com.sum.hi.common.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.sum.hi.common.R
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hilibrary.util.HiResUtil
import com.sum.hi.hiui.search.IconFontTextView
import java.lang.IllegalStateException

/**
 * @author smy
 * @date 2022/7/22
 * @desc
 */
class HiNavigationBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {
    //主副标题
    private var titleTextView: IconFontTextView? = null
    private var subTitleTextView: IconFontTextView? = null
    private var titleContainer: LinearLayout? = null

    //解析获的对象
    private var navAttrs: Attrs

    //左右按钮
    private var leftLastViewId = View.NO_ID
    private var rightLastViewId = View.NO_ID
    private val leftViewList = ArrayList<View>()
    private val rightViewList = ArrayList<View>()

    init {
        navAttrs = parseNavAttrs(context, attributeSet, defStyleAttr)
        if (!navAttrs.navTitle.isNullOrEmpty()) {
            setTitle(navAttrs.navTitle!!)
        }
        if (!navAttrs.navSubtitle.isNullOrEmpty()) {
            setSubTitle(navAttrs.navSubtitle!!)
        }
    }

    fun setNavListener(listener: OnClickListener) {
        if (!TextUtils.isEmpty(navAttrs.navIconStr)) {
            val addLeftTextButton =
                addLeftTextButton(navAttrs.navIconStr!!, R.id.id_left_back_view)
            addLeftTextButton.setOnClickListener(listener)
        }
    }

    fun setTitle(title: String) {
        ensureTitle()
        titleTextView?.text = title
        titleTextView?.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
    }

    fun setSubTitle(subtitle: String) {
        ensureSubTitle()
        updateTitleViewStyle()
        subTitleTextView?.text = subtitle
        subTitleTextView?.visibility = if (TextUtils.isEmpty(subtitle)) View.GONE else View.VISIBLE
    }

    fun ensureTitle() {
        if (titleTextView == null) {
            titleTextView = IconFontTextView(context, null)
            titleTextView?.apply {
                isSingleLine = true
                gravity = Gravity.CENTER
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.titleTextColor)

                updateTitleViewStyle()
                ensureTitleContainer()
                titleContainer?.addView(titleTextView, 0)
            }
        }
    }

    fun ensureSubTitle() {
        if (subTitleTextView == null) {
            subTitleTextView = IconFontTextView(context, null)
            subTitleTextView?.apply {
                isSingleLine = true
                gravity = Gravity.CENTER
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.titleTextColor)
                textSize = navAttrs.subTitleSize

                ensureTitleContainer()
            }
        }
    }

    private fun ensureTitleContainer() {
        if (titleContainer == null) {
            titleContainer = LinearLayout(context)
            titleContainer?.apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                val params = LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                params.addRule(RelativeLayout.CENTER_IN_PARENT)
                this@HiNavigationBar.addView(titleContainer, params)
            }
        }
    }

    private fun updateTitleViewStyle() {
        if (titleTextView != null) {
            if (subTitleTextView != null && !TextUtils.isEmpty(subTitleTextView!!.text)) {
                titleTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.titleTextSize)
                titleTextView!!.typeface = Typeface.DEFAULT_BOLD
            } else {
                titleTextView!!.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    navAttrs.titleTextSizeWithSubtitle
                )
                titleTextView!!.typeface = Typeface.DEFAULT
            }
        }
    }

    /**
     * 左侧添加按钮的能力
     */
    fun addLeftTextButton(@StringRes strRes: Int, viewId: Int): TextView {
        return addLeftTextButton(HiResUtil.string(strRes), viewId)
    }

    fun addLeftTextButton(buttonText: String, viewId: Int): TextView {
        val button = generateButton()
        button.text = buttonText
        button.id = viewId
        if (leftViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding * 2, 0, navAttrs.horPadding, 0)
        } else {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding, 0)
        }
        addLeftView(button, generateTextButtonLayoutParams())
        return button
    }

    fun addLeftView(
        view: View,
        params: LayoutParams
    ) {
        val id = view.id
        if (id == View.NO_ID) {
            throw IllegalStateException("left view must has an unique id.")
        }
        if (id == leftLastViewId) {//左侧第一个按钮
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, id)
        } else {
            params.addRule(RIGHT_OF, leftLastViewId)
        }
        leftLastViewId = id
        params.alignWithParent = true//aiginParentIfMissing
        // 当这个view依赖于另外一个view摆放位置的时候，它从这个布局中消失或者隐藏，此时让这个view处于parent的左边
        leftViewList.add(view)
        addView(view, params)
    }

    /**
     * 右侧侧添加按钮的能力
     */
    fun addRightTextButton(@StringRes strRes: Int, viewId: Int): TextView {
        return addRightTextButton(HiResUtil.string(strRes), viewId)
    }

    fun addRightTextButton(buttonText: String, viewId: Int): TextView {
        val button = generateButton()
        button.text = buttonText
        button.id = viewId
        if (rightViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding * 2, 0)
        } else {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding, 0)
        }
        addRightView(button, generateTextButtonLayoutParams())
        return button
    }

    fun addRightView(
        view: View,
        params: LayoutParams
    ) {
        val id = view.id
        if (id == View.NO_ID) {
            throw IllegalStateException("right view must has an unique id.")
        }
        if (id == rightLastViewId) {//左侧第一个按钮
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, id)
        } else {
            params.addRule(LEFT_OF, rightLastViewId)
        }
        rightLastViewId = id
        params.alignWithParent = true//aiginParentIfMissing
        // 当这个view依赖于另外一个view摆放位置的时候，它从这个布局中消失或者隐藏，此时让这个view处于parent的左边
        rightViewList.add(view)
        addView(view, params)
    }

    private fun generateTextButtonLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    }

    private fun generateButton(): TextView {
        val button = TextView(context)
        button.setBackgroundResource(0)
        button.minWidth = 0
        button.width = 0
        button.minHeight = 0
        button.height = 0
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.btnTextSize)
        button.setTextColor(navAttrs.btnTextColor)
        button.gravity = Gravity.CENTER
        return button
    }

    private fun parseNavAttrs(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int
    ): Attrs {
        //解析全局的navigationStyle
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.navigationStyle, value, true)
        //xml -》 them-navigationStyle -> navigationStyle
        val defStyleRes =
            if (value.resourceId != 0) value.resourceId else R.style.navigationStyle//默认使用navigationStyle中的属性
        val array = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.HiNavigationBar,
            defStyleAttr,
            defStyleRes
        )

        val navIcon = array.getString(R.styleable.HiNavigationBar_nav_icon)
        val navTitle = array.getString(R.styleable.HiNavigationBar_nav_title)
        val navSubTitle = array.getString(R.styleable.HiNavigationBar_nav_subtitle)

        val horPadding = array.getDimensionPixelSize(R.styleable.HiNavigationBar_hor_padding, 0)
        val btnTextSize = array.getDimensionPixelSize(
            R.styleable.HiNavigationBar_text_btn_text_size,
            HiDisplayUtil.dp2px(16f)
        )
        val btnTextColor = array.getColorStateList(R.styleable.HiNavigationBar_text_btn_text_color)

        val titleTextSize = array.getDimensionPixelSize(
            R.styleable.HiNavigationBar_title_text_size,
            HiDisplayUtil.dp2px(16f)
        )
        val titleTextSizeWithSubtitle = array.getDimensionPixelSize(
            R.styleable.HiNavigationBar_title_text_size_with_subtitle,
            HiDisplayUtil.dp2px(16f)
        )
        val titleTextColor = array.getColor(
            R.styleable.HiNavigationBar_title_text_color,
            HiResUtil.color(R.color.black)
        )

        val subTitleSize = array.getDimensionPixelSize(
            R.styleable.HiNavigationBar_subtitle_text_size,
            HiDisplayUtil.dp2px(14f)
        )
        val subTitleColor = array.getColor(
            R.styleable.HiNavigationBar_subtitle_text_color,
            HiResUtil.color(R.color.black)
        )
        array.recycle()
        return Attrs(
            navIcon,
            navTitle,
            navSubTitle,
            horPadding,
            btnTextSize.toFloat(),
            btnTextColor,
            titleTextSize.toFloat(),
            titleTextSizeWithSubtitle.toFloat(),
            titleTextColor,
            subTitleSize.toFloat(),
            subTitleColor
        )
    }

    private data class Attrs(
        val navIconStr: String?,
        val navTitle: String?,
        val navSubtitle: String?,
        val horPadding: Int,
        val btnTextSize: Float,
        val btnTextColor: ColorStateList?,
        val titleTextSize: Float,
        val titleTextSizeWithSubtitle: Float,
        val titleTextColor: Int,
        val subTitleSize: Float,
        val subTitleTextColor: Int
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (titleContainer != null) {
            //计算标题栏左侧占用空间
            var leftUseSpace = paddingLeft
            for (view in leftViewList) {
                leftUseSpace += view.measuredWidth
            }

            var rightUseSpace = paddingRight
            for (view in rightViewList) {
                rightUseSpace += view.measuredWidth
            }

            val titleContainerWidth = titleContainer!!.measuredWidth
            //为了让标题居中显示
            val remainingSpace = measuredWidth - Math.max(leftUseSpace, rightUseSpace) * 2
            if (remainingSpace < titleContainerWidth) {
                val size = MeasureSpec.makeMeasureSpec(remainingSpace, MeasureSpec.EXACTLY)
                titleContainer!!.measure(size, heightMeasureSpec)
            }
        }
    }

    /**
     * 需要判断params，如果不是RealtvieLayout的params，直接添加进来回报错
     */
    fun setContainerView(view: View) {
        var params = view.layoutParams
        if (params == null) {
            params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else if (params !is LayoutParams) {
            params = LayoutParams(params)
        }

        val containerParams = params as LayoutParams
        containerParams.addRule(RIGHT_OF, leftLastViewId)
        containerParams.addRule(LEFT_OF, rightLastViewId)
        params.addRule(CENTER_VERTICAL)
        addView(view, containerParams)
    }
}