package com.sum.hi.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.ui.hiitem.HiDataItem
import org.devio.`as`.proj.main.model.GoodsModel

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/06 00:02
 * @类描述 ${TODO}
 */
class GoodsItem(goodsModel: GoodsModel) :
    HiDataItem<GoodsModel, RecyclerView.ViewHolder>(goodsModel) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {

    }
}