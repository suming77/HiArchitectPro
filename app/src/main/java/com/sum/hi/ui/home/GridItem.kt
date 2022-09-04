package com.sum.hi.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.ui.R
import com.sum.hi.ui.databinding.LayoutHomeOpGridItemBinding
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.Subcategory

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/06 00:01
 * @类描述 ${TODO}
 */
class GridItem(val list: List<Subcategory>) :
    HiDataItem<List<Subcategory>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter = GridAdapter(context, list)
    }

    override fun getItemView(parent: ViewGroup): View? {
        val gridView = RecyclerView(parent.context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = HiDisplayUtil.dp2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(Color.WHITE)

        return gridView
    }

    inner class GridAdapter(val context: Context, val list: List<Subcategory>) :
        RecyclerView.Adapter<GridAdapter.GridItemViewHolder>() {
        private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
//            val view = layoutInflater.inflate(R.layout.layout_home_op_grid_item, parent, false)
            val binding = LayoutHomeOpGridItemBinding.inflate(layoutInflater, parent, false)
            return GridItemViewHolder(binding.root, binding)
        }

        override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
            val subcategory = list[position]
            holder.binding.subCategory = subcategory
//            holder.itemView.findViewById<ImageView>(R.id.item_image)
//                .loadUrl(subcategory.subcategoryIcon)
//            holder.itemView.findViewById<TextView>(R.id.item_title).text =
//                subcategory.subcategoryName
            holder.itemView.setOnClickListener {
                Toast.makeText(context, "you touch me:" + position, Toast.LENGTH_SHORT).show()
            }
        }

        inner class GridItemViewHolder(view:View, val binding: LayoutHomeOpGridItemBinding):HiViewHolder(view){

        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}