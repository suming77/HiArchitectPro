package com.sum.hi.ui.hiitem

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.ui.R

/**
 * @Author:   smy
 * @Date:     2022/2/14 1:19
 * @Desc:
 */
class TopBanner(dataItem: ItemData) : HiDataItem<ItemData, RecyclerView.ViewHolder>(dataItem) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val itemImage = holder.itemView.findViewById<ImageView>(R.id.iv_image)
        itemImage.setImageResource(R.drawable.fire)
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.banner_item_layout
    }
}