package com.sum.hi.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sum.hi.common.R
import com.sum.hi.common.R.color.design_default_color_secondary
import kotlin.math.roundToInt

/**
 * @创建者 mingyan.su
 * @创建时间 2022/05/08 10:27
 * @类描述 ${TODO}
 *  open 表示类可继承，否则类默认是final的；
 *  在attrs资源文件中定义属性
 */
open class InputItemLayout : LinearLayout {
//    constructor(context: Context) : super(context)
//    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
//    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
//
//    }

    private lateinit var titleView: TextView
    private lateinit var editText: EditText
    private lateinit var bottomLine: Line
    private lateinit var topLine: Line

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context, attributeSet, defStyleAttr
    ) {
        orientation = LinearLayout.HORIZONTAL
        //加载自定义属性集合
        val array =
            context.obtainStyledAttributes(attributeSet, R.styleable.InputItemLayout)
        //根据属性名称获取实际的属性值
        val titleStyleId = array.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        val title = array.getString(R.styleable.InputItemLayout_title);
        parseTitleStyle(title, titleStyleId)

        val inputStyleId = array.getResourceId(R.styleable.InputItemLayout_inputTextAppearance, 0)
        val hint = array.getString(R.styleable.InputItemLayout_hint)
        val inputType = array.getInteger(R.styleable.InputItemLayout_inputType, 0)
        parseInputStyle(hint, inputType, inputStyleId)

        val topLineStyle = array.getResourceId(R.styleable.InputItemLayout_topLineAppearance, 0)
        val bottomLineStyle =
            array.getResourceId(R.styleable.InputItemLayout_bottomLineAppearance, 0)

        topLine = parseLineStyle(topLineStyle)
        bottomLine = parseLineStyle(bottomLineStyle)

        if (topLine.enable) {
            topPain.setColor(topLine.color)
            topPain.style = Paint.Style.FILL_AND_STROKE //实线
            topPain.strokeWidth = topLine.height.toFloat()
        }

        if (bottomLine.enable) {
            bottomPain.setColor(bottomLine.color)
            bottomPain.style = Paint.Style.FILL_AND_STROKE //实线
            bottomPain.strokeWidth = bottomLine.height.toFloat()
        }


        //对象回收
        array.recycle()
    }

    /**
     * 解析上下分割线配置属性
     */
    private fun parseLineStyle(lineStyle: Int): Line {
        var line = Line()
        val array =
            context.obtainStyledAttributes(lineStyle, R.styleable.lineAppearance)
        line.color = array.getColor(
            R.styleable.lineAppearance_color,
            resources.getColor(R.color.design_default_color_surface)
        )
        //获取属性值并且转为px
        line.height = array.getDimensionPixelOffset(R.styleable.lineAppearance_height, 0)
        line.leftMargin = array.getDimensionPixelOffset(R.styleable.lineAppearance_leftMargin, 0)
        line.rightMargin = array.getDimensionPixelOffset(R.styleable.lineAppearance_rightMargin, 0)
        line.enable = array.getBoolean(R.styleable.lineAppearance_enable, false)
        array.recycle()
        return line
    }

    inner class Line {
        var color = 0
        var height = 0
        var leftMargin = 0
        var rightMargin = 0
        var enable = false
    }

    /**
     * 解析右侧输入框资源属性
     */
    private fun parseInputStyle(hint: String?, inputType: Int, inputStyleId: Int) {
        val array =
            context.obtainStyledAttributes(inputStyleId, R.styleable.inputTextAppearance)
        val hintColor = array.getColor(R.styleable.inputTextAppearance_hintColor,  ContextCompat.getColor(context, R.color.design_default_color_error))
        val inputColor = array.getColor(R.styleable.inputTextAppearance_inputColor,  ContextCompat.getColor(context, R.color.design_default_color_error))
        //px
        val textSize =
            array.getDimensionPixelSize(
                R.styleable.inputTextAppearance_textSize,
                applyUnit(TypedValue.COMPLEX_UNIT_SP, 14f)
            )

        editText = EditText(context)
        var params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        editText.layoutParams = params

        editText.hint = hint
        editText.setHintTextColor(hintColor)
        editText.setTextColor(inputColor)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.gravity = Gravity.CENTER and Gravity.START
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())

        if (inputType == 0) {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputType == 1) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or (InputType.TYPE_CLASS_TEXT)
        } else if (inputType == 2) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        addView(editText)
        array.recycle()
    }

    /**
     * 解析title资源属性
     */
    private fun parseTitleStyle(title: String?, titleStyleId: Int) {
        val array =
            context.obtainStyledAttributes(titleStyleId, R.styleable.titleTextAppearance)
        val titleColor = array.getColor(R.styleable.titleTextAppearance_titleColor,  ContextCompat.getColor(context, R.color.design_default_color_error))
        val titleSize = array.getDimensionPixelSize(
            R.styleable.titleTextAppearance_titleSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 14f)
        )
        val minWidth =
            array.getDimensionPixelOffset(R.styleable.titleTextAppearance_minWidth, 0)

        titleView = TextView(context)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())  //sp---当做sp在转换一次
//        titleView.textSize = titleSize.toFloat()
        titleView.setTextColor(titleColor)
        titleView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT
        )
        titleView.minWidth = minWidth
        titleView.gravity = Gravity.CENTER and Gravity.START
        titleView.text = title

        addView(titleView)
        array.recycle()
    }

    /**
     * dp转换为px
     * 比如 unit为dp, value为14，则会转换为px返回
     */
    private fun applyUnit(unit: Int, value: Float): Int {
        return TypedValue.applyDimension(unit, value, resources.displayMetrics).toInt()
    }

    val topPain = Paint(Paint.ANTI_ALIAS_FLAG)
    val bottomPain = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (topLine.enable) {
            canvas?.drawLine(
                topLine.leftMargin.toFloat(),
                0f,
                (measuredWidth - topLine.rightMargin).toFloat(),
                0f, topPain
            )
        }

        if (bottomLine.enable) {
            canvas?.drawLine(
                bottomLine.leftMargin.toFloat(),
                (height - bottomLine.height).toFloat(),
                (measuredWidth - bottomLine.rightMargin).toFloat(),
                (height - bottomLine.height).toFloat(),
                bottomPain
            )
        }
    }

    fun getTitleView(): TextView {
        return titleView
    }

    fun getEditText(): EditText {
        return editText
    }

}