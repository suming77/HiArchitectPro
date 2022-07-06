package com.sum.hi.ui.home

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hiui.refresh.HiScrollUtil
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem
import kotlinx.android.synthetic.main.layout_home_goods_list_item1.view.*
import org.devio.`as`.proj.main.model.GoodsModel
import org.w3c.dom.Text

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/06 00:02
 * @类描述 ${TODO}
 */
class GoodsItem(val goodsModel: GoodsModel, val hotTab: Boolean) :
    HiDataItem<GoodsModel, RecyclerView.ViewHolder>(goodsModel) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.itemView.item_image.loadUrl(goodsModel.sliderImage)
        holder.itemView.item_title.text = goodsModel.goodsName
        holder.itemView.item_price.text = goodsModel.marketPrice
        holder.itemView.item_sale_desc.text = goodsModel.completedNumText

        val itemLabelContainer = holder.itemView.item_label_container
        if (!goodsModel.tags.isNullOrEmpty()) {
            itemLabelContainer.visibility = View.VISIBLE
            val split = goodsModel.tags.split(" ")
            for (index in split.indices) {
                //这里有个复用的问题
                val labView = if (index > itemLabelContainer.childCount - 1) {
                    createLabelView(context, index != 0)
                } else {
                    itemLabelContainer.getChildAt(index) as TextView
                }
                labView.text = split[index]
            }
        } else {
            itemLabelContainer.visibility = View.GONE
        }


        //添加 中间间距， 无法使用position来区分是左边还有右边，
        //根据recyclerview的x值和item的x值比较，相等则是左边不等是右边
        if (hotTab) {
            val margin = HiDisplayUtil.dp2px(2f)
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = mAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = mAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0

            val itemLeft = holder.itemView.left
            if (itemLeft == parentLeft + parentPaddingLeft) {
                params.rightMargin = margin
            } else {
                params.leftMargin = margin
            }
            holder.itemView.layoutParams = params
        }
    }

    override fun getItemLayoutRes(): Int {
        return if (hotTab) R.layout.layout_home_goods_list_item1 else R.layout.layout_home_goods_list_item2
    }

    fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val textView = TextView(context)
        textView.textSize = 11f
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_eed))
        textView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            HiDisplayUtil.dp2px(14f)
        )
        params.leftMargin = if (withLeftMargin) HiDisplayUtil.dp2px(5f) else 0
        textView.layoutParams = params
        return textView
    }

}