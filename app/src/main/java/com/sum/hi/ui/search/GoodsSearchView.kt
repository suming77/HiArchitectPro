package com.sum.hi.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sum.hi.ui.hiitem.HiAdapter
import com.sum.hi.ui.hiitem.HiRecyclerView
import com.sum.hi.ui.home.GoodsItem

import com.sum.hi.ui.model.GoodsModel

/**
 * @author smy
 * @date   2022/9/20 12:23
 * @desc
 */
class GoodsSearchView(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
    HiRecyclerView(context, attributeSet, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        adapter = HiAdapter(context)
    }

    fun bindData(list: List<GoodsModel>, loadInit: Boolean) {
        val dataItems = mutableListOf<GoodsItem>()
        for (good in list) {
            dataItems.add(GoodsItem(good, true))
        }
        val hiAdapter = adapter as HiAdapter
        if (loadInit) hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
    }
}