package com.sum.hi.ui.hiitem

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType
import java.util.zip.Inflater

/**
 * @Author:         smy
 * @CreateDate:     2022/2/13 9:54
 * @Desc:
 */
class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context
    private var mInflater: LayoutInflater? = null
    private var dataSets = ArrayList<HiDataItem<*, RecyclerView.ViewHolder>>()
    private var typeArrays = SparseArray<HiDataItem<*, RecyclerView.ViewHolder>>()//在查询方面效率高

    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }

    fun addItem(index: Int, item: HiDataItem<*, RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }

        val notifyPos = if (index > 0) index else dataSets.size - 1
        if (notify) {
            notifyItemChanged(notifyPos)
        }
    }

    fun addItems(list: List<HiDataItem<*, RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        for (item in list) {
            dataSets.add(item)
        }

        if (notify) {
            notifyItemRangeChanged(start, list.size)
        }
    }

    fun removeItem(index: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        if (index > 0 && index < dataSets.size) {
            val remove = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return remove
        } else {
            return null
        }
    }

    fun removeItem(item: HiDataItem<*, *>?) {
        if (item != null) {
            val index = dataSets.indexOf(item)
            notifyItemRemoved(index)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataSets[position]
        val type = item.javaClass.hashCode()
        //如果还没又这种类型的viewType则添加
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, item)
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataItem = typeArrays.get(viewType)
        var itemView = dataItem.getItemView(parent)
        if (itemView == null) {
            val itemLayoutRes = dataItem.getItemLayoutRes()
            if (itemLayoutRes < 0) {
                RuntimeException("dataItem ${dataItem.javaClass.name} must override getItemView() or getItemLayoutRes()")
            }
            itemView = mInflater!!.inflate(itemLayoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, itemView)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, RecyclerView.ViewHolder>>,
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
                    //通过反射构建ViewHolder实例
                    return argument.getConstructor(View::class.java)
                        .newInstance(itemView) as RecyclerView.ViewHolder
                }
            }
        }
        //不能返回null，会报错
        return object : RecyclerView.ViewHolder(itemView!!) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSets[position]
        item.onBindData(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < dataSets.size) {
                        val hiDataItem = dataSets[position]
                        val spanSize = hiDataItem.getSpanSize()
                        return if (spanSize <= 0) spanCount else spanSize
                    }
                    return spanCount
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return dataSets.size
    }

    fun refreshItem(hiDataItem: HiDataItem<*, *>) {
        val indexOf = dataSets.indexOf(hiDataItem)
        notifyItemChanged(indexOf)
    }


}