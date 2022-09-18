package com.sum.hi.hiui.search

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hilibrary.util.HiResUtil
import com.sum.hi.hiui.R

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/17 15:31
 * @类描述 ${TODO}
 */
object SearchAttrsParse {

    fun parseSearchViewAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int): Attrs {
        //解析appthem是否有配置hiSearchViewStyle,否则使用默认的
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.hiSearchViewStyle, value, true)
        val defStyleRes = if (value.resourceId != 0) value.resourceId else R.style.hiSearchViewStyle

        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.HiSearchView,
            defStyleAttr,
            defStyleRes
        )
        //搜索背景
        val search_background = array.getDrawable(R.styleable.HiSearchView_search_background)
            ?: HiResUtil.drawable(R.drawable.shape_search_view)
        //搜索图标
        val search_icon = array.getString(R.styleable.HiSearchView_search_icon)
        val search_icon_size = array.getDimensionPixelSize(
            R.styleable.HiSearchView_search_icon_size,
            HiDisplayUtil.dp2px(16f)
        )
        val search_icon_padding = array.getDimensionPixelOffset(
            R.styleable.HiSearchView_search_icon_padding,
            HiDisplayUtil.dp2px(4f)
        )

        //清除图标
        val search_clear_icon = array.getString(R.styleable.HiSearchView_search_clear_icon)
        val search_clear_icon_size = array.getDimensionPixelSize(
            R.styleable.HiSearchView_search_clear_icon_size,
            HiDisplayUtil.dp2px(16f)
        )

        //搜索提示语
        val search_hint_text = array.getString(R.styleable.HiSearchView_search_hint_text)
        val search_hint_text_size = array.getDimensionPixelSize(
            R.styleable.HiSearchView_search_hint_text_size,
            HiDisplayUtil.dp2px(16f)
        )
        val search_hint_text_color = array.getColor(
            R.styleable.HiSearchView_search_hint_text_color,
            HiResUtil.color(R.color.color_656)
        )

        //相对位置
        val gravity = array.getInteger(R.styleable.HiSearchView_search_hint_gravity, 1)

        //输入文本
        val search_text_size = array.getDimensionPixelSize(
            R.styleable.HiSearchView_search_text_size,
            HiDisplayUtil.dp2px(16f)
        )
        val search_text_color = array.getColor(
            R.styleable.HiSearchView_search_text_color,
            HiResUtil.color(R.color.black)
        )

        //keyword关键词
        val search_keyword_size = array.getDimensionPixelSize(
            R.styleable.HiSearchView_search_keyword_size,
            HiDisplayUtil.dp2px(13f)
        )
        val search_keyword_color =
            array.getColor(R.styleable.HiSearchView_search_keyword_color, Color.WHITE)
        val search_keyword_background =
            array.getDrawable(R.styleable.HiSearchView_search_keyword_background)
        val search_keyword_max_length =
            array.getInteger(R.styleable.HiSearchView_search_keyword_max_length, 20)
        val search_keyword_padding = array.getDimensionPixelOffset(
            R.styleable.HiSearchView_search_keyword_padding,
            HiDisplayUtil.dp2px(10f)
        )
        val search_keyword_clear_icon = array.getString(R.styleable.HiSearchView_search_keyword_icon)
        val search_keyword_clear_icon_size = array.getDimensionPixelSize(
            R.styleable.HiSearchView_search_clear_icon_size,
            HiDisplayUtil.dp2px(16f)
        )
        array.recycle()

        return Attrs(
            search_background,
            search_icon,
            search_icon_size.toFloat(),
            search_icon_padding,
            search_clear_icon,
            search_clear_icon_size.toFloat(),
            search_hint_text,
            search_hint_text_size.toFloat(),
            search_hint_text_color,
            gravity,
            search_text_size.toFloat(),
            search_text_color,
            search_keyword_size.toFloat(),
            search_keyword_color,
            search_keyword_max_length,
            search_keyword_background,
            search_keyword_clear_icon,
            search_keyword_clear_icon_size.toFloat()
        )
    }
}

class Attrs(
    var searchBackground: Drawable?,
    var searchIcon: String?,
    var search_icon_size: Float,
    var searchIconPadding: Int,
    var searchClearIcon: String?,
    var search_clear_icon_size: Float,
    var searchHintText: String?,
    var search_hint_text_size: Float,
    var searchHintTextColor: Int,
    var gravity: Int,
    var search_text_size: Float,
    var searchTextColor: Int,
    var search_keyword_size: Float,
    var searchKeywordColor: Int,
    var searchKeywordMaxLength: Int,
    var searchKeywordBackground: Drawable?,
    var search_keyword_clear_icon: String?,
    var search_keyword_clear_icon_size: Float
) {

}
