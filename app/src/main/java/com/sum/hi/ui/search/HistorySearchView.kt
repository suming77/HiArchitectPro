package com.sum.hi.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sum.hi.ui.R
import com.sum.hi.ui.demo.framework.mvp.HomeContract
import kotlinx.android.synthetic.main.layout_history_search.view.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/23 21:50
 * @类描述 ${TODO}
 */
class HistorySearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val keyWords = mutableListOf<KeyWord>()
    var chipGroup: ChipGroup? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_history_search, this, true)
        chipGroup = findViewById<ChipGroup>(R.id.chip_group)
    }

    fun bindData(histories: ArrayList<KeyWord>?) {
        if (histories == null) return
        keyWords.clear()
        keyWords.addAll(histories)
        for (index in 0 until histories.size) {
            var chipItem: Chip
            //这里也要需要判断一下复用的情况
            val childCount = chipGroup?.childCount
            if (index < childCount!!) {
                chipItem = chipGroup?.getChildAt(index) as Chip
            } else {
                chipItem = generateChipItem()
                chipGroup?.addView(chipItem)
            }
            chipItem.text = histories[index].keyWord
        }
    }

    private fun generateChipItem(): Chip {
        val chipItem: Chip =
            LayoutInflater.from(context)
                .inflate(R.layout.layout_history_search_chip_item, chipGroup, false) as Chip
        chipItem.isChecked = false //刚创建的时候是没有选中的
        chipItem.isCheckable = true
        //还需要去改变每个chip的id，这个id在布局文件的时候是相同的，但是我们添加到chipGroup的时候是不同的
//        chipItem.id = View.generateViewId()
        chipItem.id = chipGroup?.childCount ?: 0
        return chipItem
    }

    /**
     * 需要暴露方法，点击item的时候把事件暴露出去
     */
    fun setOnCheckChangeListener(callBack: (KeyWord) -> Unit) {
        if (chipGroup == null) return
        chipGroup?.setOnCheckedChangeListener { group, checkedId ->
            for (index in 0 until chipGroup?.childCount!!) {
                if (chipGroup?.getChildAt(index)?.id == checkedId) {
                    callBack.invoke(keyWords[index])
                    break
                }
            }
        }
    }


    fun setOnHistoryClearListener(callBack: () -> Unit) {
        iv_delete.setOnClickListener {
            chipGroup?.removeAllViews()
            keyWords.clear()
            callBack()
        }
    }

}