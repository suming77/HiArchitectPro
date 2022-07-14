package com.sum.hi.ui.home

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.SliderImage

/**
 * @author smy
 * @date   2022/7/13 22:55
 * @desc
 */
class GoodsGalleryItem(val sliderImage: SliderImage) : HiDataItem<SliderImage, HiViewHolder>() {
    private var parentWidth: Int = 0

    override fun onBindData(holder: HiViewHolder, position: Int) {
        val imageView = holder.itemView as ImageView
        if (!sliderImage.url.isNullOrEmpty()) {

            //需要添加展位图
            //需要拿到图片加载后的回调
            //根据图片宽高的值，计算imageView的高度值
            imageView.loadUrl(sliderImage.url) {
                val drawableWidth = it.intrinsicWidth
                val drawableHeight = it.intrinsicHeight
                val params = holder.itemView.layoutParams ?: RecyclerView.LayoutParams(
                    parentWidth,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
                params.width = parentWidth
                params.height = (drawableHeight / (drawableWidth * 1.0f / parentWidth)).toInt()
                imageView.layoutParams = params
                ViewCompat.setBackground(imageView, it)
            }
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val imageView = ImageView(parent.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.white))
        return imageView
    }

    /**
     * 当getItemView添加到列表中的时候，给ImageView预设宽高值
     */
    override fun onViewAttachedToWindow(holder: HiViewHolder) {
        super.onViewAttachedToWindow(holder)
        //提前给ImageView预设一个宽高值,实际还需要根据图片宽高来设置，能满足大部分需求
        parentWidth = (holder.itemView.parent as ViewGroup).measuredWidth
        val layoutParams = holder.itemView.layoutParams
        if (holder.itemView.width == parentWidth) {
            layoutParams.height = parentWidth
            layoutParams.width = parentWidth
            holder.itemView.layoutParams = layoutParams
        }
    }
}