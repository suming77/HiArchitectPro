package com.sum.hi.ui.hiitem

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sum.hi.hilibrary.HiViewHolder
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 * @Author:         smy
 * @CreateDate:     2022/2/13 9:54
 * @Desc:
 */
class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recyclerViewRef: WeakReference<RecyclerView>? = null
    private var mContext: Context
    private var mInflater: LayoutInflater? = null
    private var dataSets = ArrayList<HiDataItem<*, out RecyclerView.ViewHolder>>()
    private var typeArrays = SparseArray<HiDataItem<*, out RecyclerView.ViewHolder>>()//在查询方面效率高

    private var heads = SparseArray<View>()
    private var foots = SparseArray<View>()

    private var BASE_ITEM_TYPE_HEAD = 100000
    private var BASE_ITEM_TYPE_FOOTER = 200000

    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }

    fun addHeadView(headView: View) {
        //没有被添加加过
        if (heads.indexOfValue(headView) < 0) {
            heads.put(BASE_ITEM_TYPE_HEAD++, headView)
            notifyItemInserted(heads.size() - 1)
        }
    }

    fun removeHeaderView(view: View) {
        val indexOfValue = heads.indexOfValue(view)
        if (indexOfValue < 0) return
        heads.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue)
    }

    fun addFooterView(view: View) {
        if (foots.indexOfValue(view) < 0) {
            foots.put(BASE_ITEM_TYPE_FOOTER++, view)
            notifyItemInserted(itemCount)
        }
    }

    fun removeFooterView(view: View) {
        val indexOfValue = foots.indexOfValue(view)
        if (indexOfValue < 0) return
        foots.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue + getHeaderSize() + getOriginalItemCount())

    }

    fun getHeaderSize(): Int {
        return heads.size()
    }

    fun getFooterSize(): Int {
        return foots.size()
    }

    fun getOriginalItemCount(): Int {
        return dataSets.size
    }

    fun addItem(index: Int, item: HiDataItem<*, out RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }

        val notifyPos = if (index > 0) index else dataSets.size - 1
        if (notify) {
            notifyItemChanged(notifyPos)
        }
        item.setAdapter(this)
    }

    fun addItems(list: List<HiDataItem<*, out RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        for (item in list) {
            dataSets.add(item)
            item.setAdapter(this)
        }

        if (notify) {
            notifyItemRangeChanged(start, list.size)
        }
    }

    fun removeItem(index: Int): HiDataItem<*, out RecyclerView.ViewHolder>? {
        if (index > 0 && index < dataSets.size) {
            val remove = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return remove
        } else {
            return null
        }
    }

    fun removeItem(item: HiDataItem<*, out RecyclerView.ViewHolder>?) {
        if (item != null) {
            val index = dataSets.indexOf(item)
            notifyItemRemoved(index)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)) {
            return heads.indexOfKey(position)
        }

        if (isFooterPosition(position)) {
            //footer的位置，应该计算一下，position= 6，headercount = 1,footersize = 1
            val footerPosition = getHeaderSize() + getOriginalItemCount() + position
            return foots.indexOfKey(footerPosition)
        }

        val itemPosition = position - getHeaderSize()
        val item = dataSets[itemPosition]
        val type = item.javaClass.hashCode()
        //如果还没有这种类型的viewType则添加
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, item)
        }
        return type
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= getOriginalItemCount() + getHeaderSize()
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < getHeaderSize()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (heads.indexOfKey(viewType) >= 0) {
            val view = heads[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }
        if (foots.indexOfKey(viewType) >= 0) {
            val view = foots[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }
        val dataItem = typeArrays.get(viewType)
        var itemView:View? = dataItem.getItemView(parent)
        if (itemView == null) {
            val itemLayoutRes = dataItem.getItemLayoutRes()
            if (itemLayoutRes < 0) {
                throw RuntimeException("dataItem ${dataItem.javaClass.name} must override getItemView() or getItemLayoutRes()")
            }
            itemView = mInflater!!.inflate(itemLayoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, itemView)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, out RecyclerView.ViewHolder>>,
        itemView: View?
    ): RecyclerView.ViewHolder {
        val superclass = javaClass.superclass//超类
        if (superclass is ParameterizedType) {//参数泛型类型
            val arguments = superclass.actualTypeArguments//泛型参数集合
            for (argument in arguments) {
                //是否为class并且rvViewHolder是其超类
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(
                        argument
                    )
                ) {
                    kotlin.runCatching {
                        //通过反射构建ViewHolder实例
                        return argument.getConstructor(View::class.java)
                            .newInstance(itemView) as RecyclerView.ViewHolder
                    }.onFailure {
                        it.printStackTrace()
                    }
                }
            }
        }
        //不能返回null，会报错
        return object : HiViewHolder(itemView!!) {}
//        return object : RecyclerView.ViewHolder(itemView!!) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) return
        val itemPosition = position - getHeaderSize()
        val item = getItem(itemPosition)
        item?.onBindData(holder, itemPosition)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
                        return spanCount
                    }
                    val itemPosition = position - getHeaderSize()
                    if (itemPosition < dataSets.size) {
                        val hiDataItem = getItem(itemPosition)
                        if (hiDataItem!=null){
                            val spanSize = hiDataItem.getSpanSize()
                            return if (spanSize <= 0) spanCount else spanSize
                        }
                    }
                    return spanCount
                }

            }
        }
    }


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val recyclerView = getAttachRecyclerView()
        if (recyclerView != null) {
            //瀑布流的item占比适配
            val position = recyclerView.getChildAdapterPosition(holder.itemView)
            val isHeaderFooter = isHeaderPosition(position) || isFooterPosition(position)
            val itemPosition = position - getHeaderSize()
            val dataItem = getItem(itemPosition) ?: return
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                val manager = recyclerView.layoutManager as StaggeredGridLayoutManager?
                if (isHeaderFooter) {
                    lp.isFullSpan = true
                    return
                }
                val spanSize = dataItem.getSpanSize()
                if (spanSize == manager!!.spanCount) {
                    lp.isFullSpan = true
                }
            }

            dataItem.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        if (isHeaderPosition(position) || isFooterPosition(position))
            return
        val itemPosition = position - getHeaderSize()
        val dataItem = getItem(itemPosition) ?: return
        dataItem.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return getOriginalItemCount() + getHeaderSize() + getFooterSize()
    }

    fun refreshItem(hiDataItem: HiDataItem<*, out RecyclerView.ViewHolder>) {
        val indexOf = dataSets.indexOf(hiDataItem)
        notifyItemChanged(indexOf)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewRef?.clear()
    }

    open fun getAttachRecyclerView(): RecyclerView? {
        return recyclerViewRef?.get()
    }

    fun getItem(position: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        if (position < 0 || position >= dataSets.size)
            return null
        return dataSets[position] as HiDataItem<*, RecyclerView.ViewHolder>
    }

    fun clearItems() {
        dataSets.clear()
        notifyDataSetChanged()
    }

}