package com.sum.hi.ui

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.hilibrary.util.ColorUtil
import com.sum.hi.hilibrary.util.HiDisplayUtil
import java.lang.Math.abs
import java.lang.Math.min

/**
 * @author smy
 * @date 2022/7/14
 * @desc
 */
class TitleScrollListener(thresholdDp: Float = 100f, val callback: (Int) -> Unit) :
    RecyclerView.OnScrollListener() {
    private var lastFraction = 0f
    private val thresholdPx = HiDisplayUtil.dp2px(thresholdDp)
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        //这里需要获取列表滑动的距离。，然后与thresholdPx做运算，然后计算当前滑动状态
        //计算出一个新的颜色值，transpanrent --> white 渐变
        //recyclerView.scrollY
        //dy这两种都不够准确
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(0) ?: return
        val top = abs(viewHolder.itemView.top).toFloat()
        //计算当前滑动百分比
        val fraction = top / thresholdPx
        if (lastFraction > 1f){
            lastFraction = fraction
            return
        }
        val newColor = ColorUtil.getCurrentColor(Color.TRANSPARENT, Color.WHITE, min(fraction, 1f))
        callback(newColor)
        lastFraction = fraction
    }
}