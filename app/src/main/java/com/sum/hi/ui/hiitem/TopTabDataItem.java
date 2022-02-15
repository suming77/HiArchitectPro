package com.sum.hi.ui.hiitem;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.sum.hi.ui.R;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: smy
 * @CreateDate: 2022/2/14 1:14
 * @Desc:
 */
public class TopTabDataItem extends HiDataItem<ItemData, RecyclerView.ViewHolder> {

    public TopTabDataItem(ItemData itemData) {
        super(itemData);
    }

    @Override
    public void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.iv_image);
        imageView.setImageResource(R.drawable.fire);
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.hi_banner_item_image;
    }
}
