package com.sum.hi.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiAdapter
import com.sum.hi.ui.hiitem.HiDataItem
import kotlinx.android.synthetic.main.layout_quick_search_list_item.view.*

/**
 * @author smy
 * @date   2022/9/20 08:01
 * @desc
 */
class QuickSearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttrs) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        adapter = HiAdapter(context)
    }

    /**
     * 可以通过callback回传通过item点击的事件，回传的是KeyWord
     */
    fun bindData(keyWords: List<KeyWord>, callback: (KeyWord) -> Unit) {
        val dataItems = mutableListOf<QuickSearchItem>()
        for (keyWord in keyWords) {
            dataItems.add(QuickSearchItem(keyWord, callback))
        }
        val hiAdapter = adapter as HiAdapter
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, false)
    }

    private inner class QuickSearchItem(val keyWord: KeyWord, val callback: (KeyWord) -> Unit) :
        HiDataItem<KeyWord, HiViewHolder>() {
        override fun onBindData(holder: HiViewHolder, position: Int) {
           val item_title = holder.findViewById<TextView>(R.id.item_title)
            item_title?.text = keyWord.keyWord
            holder.itemView.setOnClickListener {
                callback(keyWord)
            }
        }

        override fun getItemLayoutRes(): Int {
            return R.layout.layout_quick_search_list_item
        }


    }
}