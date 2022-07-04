package com.sum.hi.hilibrary

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.hilibrary.util.AppGlobals
import com.sum.hi.hilibrary.util.HiDisplayUtil

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/02 23:39
 * @类描述 ${TODO}
 */
class CategoryItemDecoration(val callBack: (Int) -> String, val spanCount: Int) :
    RecyclerView.ItemDecoration() {
    private val groupFirstPosition = mutableMapOf<String, Int>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.textSize = 50f
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //1.根据view对象找到它在列表中的位置，adapterPosition
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) return

        //2.拿到当前位置adapterPosition 的GroupName
        val groupName = callBack(adapterPosition)
        //3.拿到上一个位置的GroupName
        val preGroupName = if (adapterPosition > 0) callBack(adapterPosition - 1) else null

        val sameGroup = TextUtils.equals(groupName, preGroupName)
        if (!sameGroup && !groupFirstPosition.containsKey(groupName)) {
            //就说明当前位置adapterPosition对应的item是当前组的第一个位置
            //存储起来方便后面计算，计算后面item是否为第一行
            groupFirstPosition[groupName] = adapterPosition
        }

        val firstRowPosition = groupFirstPosition[groupName] ?: 0
        val samRow = adapterPosition - firstRowPosition in 0 until spanCount

        if (!sameGroup && samRow) {
            outRect.set(0, HiDisplayUtil.dp2px(40f, view.resources), 0, 0)
            return
        }
        outRect.set(0, 0, 0, 0)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val adapterPosition = parent.getChildAdapterPosition(view)
            if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) continue
            val groupName = callBack(adapterPosition)

            //判断当前位置是否为分组的第一个位置
            //如果是，则在位置上绘制标题
            val groupFirstPosition = groupFirstPosition[groupName]
            if (groupFirstPosition == adapterPosition) {
                val decorationBounds = Rect()
                //为了拿到当前item的左上右下的坐标信息，包含了margin和拓展空间
                parent.getDecoratedBoundsWithMargins(view, decorationBounds)
                val textBounds = Rect()
                paint.getTextBounds(groupName, 0, groupName.length, textBounds)
                c.drawText(
                    groupName,
                    HiDisplayUtil.dp2px(16f, parent.resources).toFloat(),
                    (decorationBounds.top + 2 * textBounds.height()).toFloat(),
                    paint
                )
            }
        }
    }

    fun clear() {
        groupFirstPosition.clear()
    }
}