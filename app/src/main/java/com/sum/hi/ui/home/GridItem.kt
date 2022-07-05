package com.sum.hi.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.ui.hiitem.HiDataItem
import org.devio.`as`.proj.main.model.Subcategory

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/06 00:01
 * @类描述 ${TODO}
 */
class GridItem(list: List<Subcategory>) :
    HiDataItem<List<Subcategory>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {

    }
}