package com.sum.hi.ui.home

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiAdapter
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.DetailModel
import com.sum.hi.ui.model.GoodsModel
import kotlinx.android.synthetic.main.layout_detail_item_shop.view.*
import kotlinx.android.synthetic.main.layout_detail_item_shop_goods.view.*

/**
 * @author smy
 * @date 2022/7/13
 * @desc
 */
class ShopItem(val detailModel: DetailModel) : HiDataItem<DetailModel, RecyclerView.ViewHolder>() {
    private val SHOP_GOODS_ITEM_SPAN_COUNT = 3
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val shop = detailModel.shop
        shop?.let {
            holder.itemView.shop_logo.loadUrl(it.logo)
            holder.itemView.shop_title.text = it.name
            holder.itemView.shop_desc.text = "商品数量：${it.goodsNum} 已拼：${it.completeNum}"
        }

        val evaluation = shop.evaluation
        evaluation?.let {
            val tagContainer = holder.itemView.tag_container
            tagContainer.visibility = View.VISIBLE
            val serverTags = it.split(" ")
            var index = 0
            //在recyclerView就会有上下的滑动复用问题
            //6个元素，只取三个， 描述相符 高 服务态度 高 物流服务 中
            for (tagIndex in 0 until serverTags.size / 2) {
                val tagView = if (tagIndex < tagContainer.childCount) {
                    tagContainer.getChildAt(tagIndex) as TextView
                } else {
                    val textView = TextView(context)
                    val layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.weight = 1f
                    textView.layoutParams = layoutParams
                    textView.setTextColor(ContextCompat.getColor(context, R.color.color_666))
                    textView.gravity = Gravity.CENTER
                    textView.textSize = 14f

                    tagContainer.addView(textView)
                    textView
                }

                val serviceName = if (index >= serverTags.size) continue else serverTags[index]
                val serviceTag = serverTags[index + 1]
                index += 2

                val spanTag = spanServiceTag(context, serviceName, serviceTag)
                tagView.text = spanTag
            }
        }

        val flowGoods = detailModel.flowGoods
        flowGoods?.let {
            val flowRecyclerView = holder.itemView.recycler_view
            flowRecyclerView.visibility = View.VISIBLE
            if (flowRecyclerView.layoutManager == null) {
                flowRecyclerView.layoutManager =
                    GridLayoutManager(context, SHOP_GOODS_ITEM_SPAN_COUNT)
            }

            if (flowRecyclerView.adapter == null) {
                flowRecyclerView.adapter = HiAdapter(context)
            }
            val dataItems = mutableListOf<GoodsItem>()
            it.forEach {
                dataItems.add(ShopGoodsItem(it))
            }
            val adapter = flowRecyclerView.adapter as HiAdapter
            adapter.clearItems()
            adapter.addItems(dataItems, true)
        }
    }

    /**
     * 这里的RecyclerView.ViewHolder不能改为HiViewHolder,因为这里ShopGoodsItem并没有直接显性指定ViewHolder的类型
     * 那么在运行的时候，hiAdapter的createViewHolderInternal()就无法在GoodsItem中取到具体的viewHolder的class类型
     * 那么就会默认返回RecyclerView.ViewHolder,那么就会发生类型强转异常。对于ShopGoodsItem只要不显性指定类型，那么
     * 在运行时无法拿到实际类型创建viewHolder的，尽管它的父类指定了HiViewHolder,但是父类里面的泛型是会被察除的，
     * 只有子类的泛型类型才不会被察除，所以我们是无法拿到的，最简单的解决办法就是在createViewHolderInternal()娶不到
     * 具体类型时，默认返回HiViewHolder
     *
     */
    inner class ShopGoodsItem(goodsModel: GoodsModel) : GoodsItem(goodsModel, false) {
        override fun getItemLayoutRes(): Int {
            return R.layout.layout_detail_item_shop_goods
        }

        /**
         * 动态去改变图片宽高的值
         */
        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            val viewParent: ViewGroup = holder.itemView.parent as ViewGroup
            val availableWidth =
                viewParent.measuredWidth - viewParent.paddingLeft - viewParent.paddingRight
            val itemWith = availableWidth / SHOP_GOODS_ITEM_SPAN_COUNT
            val itemImage = holder.itemView.item_image
            val layoutParams = itemImage.layoutParams
            layoutParams.width = itemWith
            layoutParams.height = itemWith
            itemImage.layoutParams = layoutParams
        }
    }

    private fun spanServiceTag(
        context: Context,
        serviceName: String,
        serviceTag: String?
    ): CharSequence {
        if (serviceTag.isNullOrEmpty()) return ""
        val ss = SpannableString(serviceTag)
        val ssb = SpannableStringBuilder()

        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_c61)),
            0,
            ss.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            BackgroundColorSpan(ContextCompat.getColor(context, R.color.color_f8e)),
            0,
            ss.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.append(serviceName).append(ss)
        return ssb
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_shop
    }
}