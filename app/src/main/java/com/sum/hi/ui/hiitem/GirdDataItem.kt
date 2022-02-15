package com.sum.hi.ui.hiitem

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.ui.R

/**
 * @Author:   smy
 * @Date:     2022/2/14 1:23
 * @Desc:
 */
class GirdDataItem(dataItem: ItemData) : HiDataItem<ItemData, GirdDataItem.MyHolder>(dataItem) {
    override fun getItemLayoutRes(): Int {
        return R.layout.banner_item_layout
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null

        init {
            imageView = itemView.findViewById<ImageView>(R.id.iv_image)
        }
    }

    override fun onBindData(holder: MyHolder, position: Int) {
        holder.imageView?.setImageResource(R.drawable.fire)
    }
}
