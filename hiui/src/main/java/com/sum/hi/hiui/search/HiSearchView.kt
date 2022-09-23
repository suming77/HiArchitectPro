package com.sum.hi.hiui.search

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.sum.hi.hilibrary.util.MainHandler
import com.sum.hi.hiui.R

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/17 16:52
 * @类描述 ${TODO}
 * 为什么不直接写在布局里面，因为解析布局需要一定的耗时，而且直接将控件写在这里更容易理解
 */
class HiSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val LEFT = 1
        const val CENTER = 0
        const val DEBOUNCE_TRIGGER_DURATION = 200L
    }

    private var simpleTextWatcher: SimpleTextWatcher? = null
    var editText: EditText? = null

    //搜索小图标和默认提示语，以及container
    private var searchIcon: IconFontTextView? = null
    private var hintTv: TextView? = null
    private var searchIconHintContainer: LinearLayout? = null

    //右侧清除小图标
    private var clearIcon: IconFontTextView? = null

    //keyword
    private var keyWordContainer: LinearLayout? = null
    private var keyWordTv: TextView? = null
    private var keyWordClearIcon: IconFontTextView? = null
    val viewAttrs: Attrs = SearchAttrsParse.parseSearchViewAttrs(context, attrs, defStyleAttr)

    init {
        //初始化editText -create-bing property --addView
        initEditText()
        //初始化右侧一键清楚的小图标 -create-bing property --addView
        initClearIcon()
        //初始化 默认提示语和searchIcon -create-bing property --addView
        initSearchIconHintContainer()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            background = viewAttrs.searchBackground
        }
        editText?.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasContent = s?.length ?: 0 > 0
                clearIcon?.visibility = if (hasContent) View.VISIBLE else View.GONE
                searchIconHintContainer?.visibility = if (hasContent) View.GONE else View.VISIBLE

                //不能马上回调，需要延迟一段时间
                simpleTextWatcher?.apply {
                    MainHandler.remove(debounceRunnable)
                    MainHandler.postDelay(DEBOUNCE_TRIGGER_DURATION, debounceRunnable)
                }
            }
        })

    }

    private val debounceRunnable = Runnable {
        simpleTextWatcher?.afterTextChanged(editText?.text)
    }

    fun setDebounceTextChangeListener(simpleTextWatcher: SimpleTextWatcher) {
        this.simpleTextWatcher = simpleTextWatcher
    }

    /**
     * 当用户点击时，联想词面板的时候，会调用改方法，把关键词设置到上面
     */
    fun setKeyWord(keyWord: String, listener: OnClickListener) {
        ensureKeyWordContainer()
        toggleSearchViewsVisibility(true)
        //清空文本
        editText?.text = null
        keyWordTv?.text = keyWord
        keyWordClearIcon?.setOnClickListener {
            //点击了keywordclearicon, 此时应该恢复默认提示语
            toggleSearchViewsVisibility(false)
            listener.onClick(it)
        }
    }

    fun setClearIconClickListener(listener: OnClickListener) {
        clearIcon?.setOnClickListener {
            editText?.text = null
            clearIcon?.visibility = View.GONE
            searchIcon?.visibility = View.VISIBLE
            hintTv?.visibility = View.VISIBLE
            searchIconHintContainer?.visibility = View.VISIBLE
        }
    }

    fun setHintText(hintText: String) {
        hintTv?.text = hintText
    }


    private fun toggleSearchViewsVisibility(showKeyWord: Boolean) {
        editText?.visibility = if (showKeyWord) View.GONE else View.VISIBLE
        clearIcon?.visibility = if (showKeyWord) View.GONE else View.VISIBLE
        searchIconHintContainer?.visibility = if (showKeyWord) View.GONE else View.VISIBLE
        searchIcon?.visibility = if (showKeyWord) View.GONE else View.VISIBLE
        hintTv?.visibility = if (showKeyWord) View.GONE else View.VISIBLE

        keyWordContainer?.visibility = if (showKeyWord) View.VISIBLE else View.GONE
    }

    private fun ensureKeyWordContainer() {
        if (keyWordContainer != null) return
        //search_keyword_clear_icon不为空才去初始化
        if (!TextUtils.isEmpty(viewAttrs.search_keyword_clear_icon)) {
            keyWordClearIcon = IconFontTextView(context, null)
            keyWordClearIcon?.setText(viewAttrs.search_keyword_clear_icon)
            keyWordClearIcon?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                viewAttrs.search_keyword_size
            )
            keyWordClearIcon?.setTextColor(viewAttrs.searchKeywordColor)
            keyWordClearIcon?.id = R.id.id_search_keyword_clear_icon
            keyWordClearIcon?.setPadding(
                viewAttrs.searchIconPadding,
                viewAttrs.searchIconPadding / 2,
                viewAttrs.searchIconPadding,
                viewAttrs.searchIconPadding / 2
            )
        }


        keyWordTv = TextView(context)
        keyWordTv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.search_keyword_size)
        keyWordTv?.setTextColor(viewAttrs.searchKeywordColor)
        keyWordTv?.includeFontPadding = false
        keyWordTv?.isSingleLine = true
        keyWordTv?.ellipsize = TextUtils.TruncateAt.END
        //控制字符多少 一个数组，InputFilter 的LengthFilter
        keyWordTv?.filters =
            arrayOf(InputFilter.LengthFilter(viewAttrs.searchKeywordMaxLength))
        keyWordTv?.id = R.id.id_search_keyword_text_view
        keyWordTv?.setPadding(
            viewAttrs.searchIconPadding,
            viewAttrs.searchIconPadding / 2,
            0,
            viewAttrs.searchIconPadding
        )

        keyWordContainer = LinearLayout(context)
        keyWordContainer?.background = viewAttrs.searchKeywordBackground
        keyWordContainer?.orientation = LinearLayout.HORIZONTAL
        keyWordContainer?.gravity = Gravity.CENTER

        keyWordContainer?.addView(
            keyWordTv,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        if (keyWordClearIcon != null) {
            keyWordContainer?.addView(
                keyWordClearIcon,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

        val kwParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        kwParams.addRule(CENTER_VERTICAL)
        kwParams.addRule(ALIGN_PARENT_LEFT)
        kwParams.leftMargin = viewAttrs.searchIconPadding
        kwParams.rightMargin = viewAttrs.searchIconPadding
        addView(keyWordContainer, kwParams)
    }


    private fun initSearchIconHintContainer() {
        //HintView  - start
        hintTv = TextView(context)
        hintTv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.search_hint_text_size)
        hintTv?.setTextColor(viewAttrs.searchHintTextColor)
        hintTv?.isSingleLine = true
        hintTv?.text = viewAttrs.searchHintText
        hintTv?.id = R.id.id_search_hint_view

        //searchicon start
        searchIcon = IconFontTextView(context, null)
        searchIcon?.text = viewAttrs.searchIcon
        searchIcon?.setTextColor(viewAttrs.searchHintTextColor)
        searchIcon?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.search_icon_size)
        searchIcon?.id = R.id.id_search_icon
        searchIcon?.setPadding(0, 0, viewAttrs.searchIconPadding / 2, 0)

        //searchicon end
        searchIconHintContainer = LinearLayout(context)
        searchIconHintContainer?.gravity = Gravity.CENTER
        searchIconHintContainer?.orientation = LinearLayout.HORIZONTAL
        searchIconHintContainer?.addView(searchIcon)
        searchIconHintContainer?.addView(hintTv)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_VERTICAL)
        when (viewAttrs.gravity) {
            CENTER -> params.addRule(CENTER_IN_PARENT)
            LEFT -> params.addRule(ALIGN_PARENT_LEFT)
            else -> throw IllegalStateException("not support gravity now.")
        }
        addView(searchIconHintContainer, params)


    }

    private fun initClearIcon() {
        if (TextUtils.isEmpty(viewAttrs.searchClearIcon)) return
        clearIcon = IconFontTextView(context, null)
        clearIcon?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.search_clear_icon_size)
        clearIcon?.text = viewAttrs.searchClearIcon
        //清除按钮的颜色和输入文本颜色一致，如果需要不一致则额外拓展属性就可以了
        clearIcon?.setTextColor(viewAttrs.searchTextColor)
        clearIcon?.setPadding(viewAttrs.searchIconPadding, 0, 0, 0)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_VERTICAL)
        params.addRule(ALIGN_PARENT_RIGHT)//靠右显示
        //默认隐藏，只有输出才显示
        clearIcon?.visibility = View.GONE
        clearIcon?.id = R.id.id_search_clear_icon
        addView(clearIcon, params)
    }

    private fun initEditText() {
        editText = EditText(context)
        editText?.setTextColor(viewAttrs.searchTextColor)
        editText?.setBackgroundColor(Color.TRANSPARENT)
        //指定为px单位
        editText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.search_text_size)
        //防止文字输入过于贴近输入两边
        editText?.setPadding(viewAttrs.searchIconPadding, 0, viewAttrs.searchIconPadding, 0)

        //不能直接写 0， 也不能View.generateViewId()，它是api17以后才有的
        //我们需要去ids.xml文件中定义几个id的值
//        editText?.id = 0
//        editText?.id = View.generateViewId()
        editText?.id = R.id.id_search_edit_view

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        //添加要由 RelativeLayout 解释的布局规则。
        params.addRule(CENTER_VERTICAL)
        addView(editText, params)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        MainHandler.remove(debounceRunnable)
    }

    fun getKeyWord(): String? {
        return keyWordTv?.text.toString()
    }
}