package com.sum.hi.ui.home

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.hiui.banner.HiBanner
import com.sum.hi.hiui.banner.core.HiBannerMo
import com.sum.hi.hiui.banner.core.HiCircleIndicator
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.DetailModel
import com.sum.hi.ui.model.SliderImage

/**
 * @author smy
 * @date   2022/7/12 22:20
 * @desc
 */
class GoodsDetailHeaderItem(
    val sliderImages: List<SliderImage>?,
    val price: String,
    val completedNumText: String,
    val goodsName: String
) : HiDataItem<DetailModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val bannerItems = mutableListOf<HiBannerMo>()
        sliderImages?.forEach {
            val bannerMo = object : HiBannerMo() {}
            bannerMo.url = it.url
            bannerItems.add(bannerMo)
        }
        val hiBanner = holder.itemView.findViewById<HiBanner>(R.id.hi_banner)
        val tvPrice = holder.itemView.findViewById<TextView>(R.id.price)
        val saleDesc = holder.itemView.findViewById<TextView>(R.id.sale_desc)
        val title = holder.itemView.findViewById<TextView>(R.id.title)

        hiBanner.setHiIndicator(HiCircleIndicator(hiBanner.context))
        hiBanner.setBannerData(bannerItems)
        hiBanner.setBindAdapter { viewHolder, mo, position ->
            val imageView = viewHolder?.rootView as? ImageView
            mo.url?.let {
                imageView?.loadUrl(mo.url)
            }
        }

        tvPrice.text = spanPrice(price)
        saleDesc.text = completedNumText
        title.text = goodsName
    }

    fun spanPrice(price: String?): CharSequence {
        if (TextUtils.isEmpty(price)) {
            return ""
        }

        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18, true), 1, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_header
    }
}