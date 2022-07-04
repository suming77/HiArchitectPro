package com.sum.hi.ui.hiitem

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.ui.R
import kotlinx.android.synthetic.main.layout_content_loading_view.view.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/03 15:04
 * @类描述 ${TODO}
 */
class HiRecyclerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {
    private var loadMoreScrollListener: LoadMoreScrollListener? = null
    private var footerView: View? = null
    private var isLoadMore: Boolean = false

    inner class LoadMoreScrollListener(val prefetchSize: Int, val callBack: () -> Unit) :
        OnScrollListener() {
        //会有前置检查
        val adapter = getAdapter() as HiAdapter
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            //需要根据当前滑动的状态已决定要不要添加footer view 要不要执行上拉加载分页动作

            if (isLoadMore) {
                return
            }

            //需要判断当前类表上已经显示的item个数，如果列表上已显示的item个数小于0
            val totalItemCount = adapter.itemCount
            if (totalItemCount <= 0) {
                return
            }

            //此时，需要在滑动状态为 拖动状态时，就要判断要不要添加footer
            //目的就是为了防止滑动到底部时，footerView还没有显示出来
            //1.依旧需要判断是否能向下滑动，那么如何判断RecyclerView是否可以继续向下滑动

            val canScrollVertically = canScrollVertically(1)
            // 特殊情况，滑动到底部，但是加载失败了
            val lastVisibleItem = findLastVisibleItem(recyclerView)
            if (lastVisibleItem <= 0) {
                return
            }
            val arriveBottom = lastVisibleItem >= totalItemCount - 1//最后一个位置是14，总共数目是15
            //可以向下滑动，或者到底部时拖动，可以分页
            if (newState == SCROLL_STATE_DRAGGING && (canScrollVertically || arriveBottom)) {
                addFooterView()
            }

            //不能在滑动停止了才加载footerView
            if (newState != SCROLL_STATE_IDLE) {
                return
            }

            //预加载
            val arrivePrefetchPosition = totalItemCount - lastVisibleItem <= prefetchSize
            if (!arrivePrefetchPosition) {
                return
            }

            isLoadMore = true
            callBack()

        }

        private fun addFooterView() {
            val footerView = getFooterView()

            //边界情况处理，多次addFooterView会可能出现，尚未移除完成，又添加footerView进去了。报错
            if (footerView.parent != null) {
                footerView.post {
                    addFooterView()
                }
            } else {
                adapter.addFooterView(footerView)
            }
        }

        private fun findLastVisibleItem(recyclerView: RecyclerView): Int {
            when (val manager = layoutManager) {
                //GridLayoutManager 是 LinearLayoutManager的子类
                is LinearLayoutManager -> {
                    return manager.findLastVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    return manager.findLastVisibleItemPositions(null)[0]
                }
                else -> {
                    return -1
                }
            }
        }
    }


    private fun getFooterView(): View {
        if (footerView == null) {
            footerView = LayoutInflater.from(context)
                .inflate(R.layout.layout_footer_loading, this@HiRecyclerView, false)
        }
        return footerView!!
    }

    fun enableLoadMore(callBack: () -> Unit, prefetchSize: Int) {
        if (adapter !is HiAdapter) {
            HiLog.e("enableLoadMore must use HiAdapter")
            return
        }
        loadMoreScrollListener = LoadMoreScrollListener(prefetchSize, callBack)
        addOnScrollListener(loadMoreScrollListener!!)
    }

    fun disableLoadMore() {
        if (adapter !is HiAdapter) {
            HiLog.e("enableLoadMore must use HiAdapter")
            return
        }
        footerView?.let {
            if (it.parent != null) {
                (adapter as HiAdapter)?.removeFooterView(footerView!!)
            }
        }

        loadMoreScrollListener?.let {
            removeOnScrollListener(loadMoreScrollListener!!)
            loadMoreScrollListener = null
            footerView = null
            isLoadMore = false
        }
    }

    fun isLoadingMore(): Boolean {
        return isLoadingMore()
    }

    fun loadFinished(success: Boolean) {
        if (adapter !is HiAdapter) {
            HiLog.e("loadFinished must use HiAdapter")
            return
        }
        isLoadMore = false
        val hiAdapter = adapter as HiAdapter
        if (!success) {
            footerView?.let {
                if (footerView!!.parent != null) {
                    hiAdapter.removeFooterView(footerView!!)
                }
            }
        } else {
            //nothing to do ,会把加载更多挤下去，看不见
        }
    }

}