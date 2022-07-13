package com.sum.hi.ui.home

import android.view.LayoutInflater
import android.view.View
import com.sum.hi.common.view.InputItemLayout
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.DetailModel
import kotlinx.android.synthetic.main.layout_detail_item_attr.view.*

/**
 * @author smy
 * @date 2022/7/13
 * @desc
 */
class GoodsAttrItem(val detailModel: DetailModel) :HiDataItem<DetailModel, HiViewHolder>(){
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context?:return
        val goodAttr = detailModel.goodAttr
        goodAttr?.let {
            val iterator = it.iterator()
            var index = 0
            val attrContainer = holder.itemView.attr_container
            attrContainer.visibility = View.VISIBLE
            while (iterator.hasNext()){
                val attr = iterator.next()
                //entries中最多只有一个
                val entries = attr.entries
                val key = entries.first().key
                val value = entries.first().value
                val attrItemView:InputItemLayout = if (index<attrContainer.childCount){
                    attrContainer.getChildAt(index) as InputItemLayout
                }else{
                    val view = LayoutInflater.from(context).inflate(R.layout.layout_detail_item_attr_item,attrContainer, false) as InputItemLayout
                    attrContainer.addView(view)
                    view
                }

                attrItemView.getEditText().hint = value
                attrItemView.getEditText().isEnabled = false
                attrItemView.getTitleView().text = key
                index++
            }
        }

        detailModel.goodsDescription?.let {
            val attrDesc = holder.itemView.attr_desc
            attrDesc.text = it
            attrDesc.visibility = View.VISIBLE
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_attr
    }
}