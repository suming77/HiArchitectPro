package com.sum.hi.hiui.amount

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hiui.R

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/30 08:27
 * @类描述 ${TODO}
 */
class AmountView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var amountValueChangeCallback: (Int) -> Unit
    private var amountValue: Int = 1
    private var avAttrs: Attrs

    init {
        avAttrs = parseAmountViewAttrs(context, attrs, defStyleAttr)
        amountValue = avAttrs.amountValue
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        applyAttrs()
    }

    private fun applyAttrs() {
        val increaseBtn = generateBtn()
        increaseBtn.text = "+"
        val decreaseBtn = generateBtn()
        decreaseBtn.text = "—"
        val amountTv = generateTv()
        amountTv.text = amountValue.toString()

        addView(decreaseBtn)
        addView(amountTv)
        addView(increaseBtn)

        decreaseBtn.setOnClickListener {
            amountValue--
            amountTv.text = amountValue.toString()
            decreaseBtn.isEnabled = amountValue > avAttrs.amountMinValue
            increaseBtn.isEnabled = true
            amountValueChangeCallback.invoke(amountValue)
        }

        increaseBtn.setOnClickListener {
            amountValue++
            amountTv.text = amountValue.toString()
            increaseBtn.isEnabled = amountValue < avAttrs.amountMaxValue
            decreaseBtn.isEnabled = true
            amountValueChangeCallback.invoke(amountValue)
        }


    }

    private fun generateTv(): TextView {
        val textView = TextView(context)
        textView.setPadding(0, 0, 0, 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, avAttrs.amountTextSize)
        textView.setBackgroundColor(avAttrs.amountBackground)
        textView.setTextColor(avAttrs.amountTextColor)
        textView.includeFontPadding = false
        textView.gravity = Gravity.CENTER

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.leftMargin = avAttrs.margin
        params.rightMargin = avAttrs.margin
        textView.layoutParams = params
        textView.minWidth = avAttrs.amountSize
        return textView
    }

    /**
     * 如果项目里面用到是是图片，你也可以拓展一下当解析到图片属性不为空的时候就去创建ImageViewButton
     */
    private fun generateBtn(): Button {
        val btn = Button(context)
        btn.setBackgroundResource(0)
        //这里的主题是Meaterl component，它会为普通的weight设置一个内间距的效果
        btn.setPadding(0,0,0,0)
        btn.includeFontPadding = false // 不显示文字内间距
        btn.setTextColor(avAttrs.btnTextColor)

        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, avAttrs.btnTextSize)
        btn.setBackgroundColor(avAttrs.btnBackground)
        btn.gravity = Gravity.CENTER

        btn.layoutParams = ViewGroup.LayoutParams(avAttrs.btnSize, avAttrs.btnSize)
        return btn
    }

    fun getAmountValue(): Int {
        return amountValue
    }

    fun setAmountValueChangeListener(amountValueChangeCallback: (Int) -> Unit) {
        this.amountValueChangeCallback = amountValueChangeCallback;
    }

    fun parseAmountViewAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int): Attrs {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.AmountView,
            defStyleAttr,
            R.style.hiAmountViewStyle
        )

        // getDimension 与 getDimensionPixelOffset类似，前者返回值类型是Float,后者是Int；他们的值如果是dp,sp类型则需要乘以density，px则不用
        //getDimensionPixelSize 返回值类型是Int,它无论是输入的值是dp,sp,还是px，都会乘以density
        val btnTextSize = array.getDimensionPixelSize(
            R.styleable.AmountView_btn_text_size,
            HiDisplayUtil.dp2px(14f)
        )
        val btnSize =
            array.getDimensionPixelSize(R.styleable.AmountView_btn_size, HiDisplayUtil.dp2px(20f))
        val btnTextColor = array.getColorStateList(R.styleable.AmountView_btn_color)
        val btnBackground =
            array.getColor(R.styleable.AmountView_btn_background, Color.parseColor("#eeeeee"))
        val margin = array.getDimensionPixelOffset(R.styleable.AmountView_btn_margin, 0)

        val amountTextSize = array.getDimensionPixelSize(
            R.styleable.AmountView_amount_text_size,
            HiDisplayUtil.dp2px(14f)
        )
        val amountTextColor = array.getColor(R.styleable.AmountView_amount_color, Color.BLACK)
        val amountSize = array.getDimensionPixelSize(
            R.styleable.AmountView_amount_size,
            HiDisplayUtil.dp2px(20f)
        )
        val amountBackground = array.getColor(R.styleable.AmountView_amount_background, Color.WHITE)

        val amountValue = array.getInteger(R.styleable.AmountView_value, 1)
        val amountMinValue = array.getInteger(R.styleable.AmountView_min_value, 1)
        val amountMaxValue = array.getInteger(R.styleable.AmountView_max_value, Int.MAX_VALUE)

        array.recycle()
        return Attrs(
            btnTextSize.toFloat(),
            btnTextColor,
            btnSize,
            margin,
            btnBackground,
            amountTextSize.toFloat(),
            amountTextColor,
            amountSize,
            amountBackground,
            amountValue,
            amountMinValue,
            amountMaxValue
        )
    }

    data class Attrs(
        val btnTextSize: Float,
        val btnTextColor: ColorStateList?,
        val btnSize: Int,
        val margin: Int,
        val btnBackground: Int,
        val amountTextSize: Float,
        val amountTextColor: Int,
        val amountSize: Int,
        val amountBackground: Int,
        val amountValue: Int,
        val amountMinValue: Int,
        val amountMaxValue: Int
    )
}