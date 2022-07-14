package com.sum.hi.ui.home

import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem

/**
 * @author smy
 * @date 2022/7/14
 * @desc
 */
class GoodsSimilarTitleItem:HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}