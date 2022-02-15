package com.sum.hi.ui.hiitem

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

/**
 * @Author:         smy
 * @CreateDate:     2022/2/13 0:01
 * @Desc:
 */
abstract class HiDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA) {
    private lateinit var mAdapter: HiAdapter
    var mData: DATA? = null

    init {
        mData = data
    }

    abstract fun onBindData(holder: VH, position: Int)

    /**
     * 返回该item的布局资源id
     */
    open fun getItemLayoutRes(): Int {
        return -1
    }

    /**
     * 返回该item的视图view
     */
    open fun getItemView(parent: ViewGroup): View? {
        return null
    }

    /**
     * 刷新列表
     */
    fun refreshItem() {
        mAdapter.refreshItem(this)
    }

    /**
     * 从列表上移除
     */
    fun removeItem() {
        mAdapter.removeItem(this)
    }

    /**
     * 该Item在列表上占据几列
     */
    fun getSpanSize(): Int {
        return 0
    }

    fun setAdapter(adapter: HiAdapter){
        this.mAdapter = adapter
    }
}