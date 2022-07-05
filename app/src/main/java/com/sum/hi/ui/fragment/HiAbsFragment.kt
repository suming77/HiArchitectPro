package com.sum.hi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.component.HiBaseFragment
import com.sum.hi.common.view.EmptyView
import com.sum.hi.hiui.refresh.HiOverView
import com.sum.hi.hiui.refresh.HiRefresh
import com.sum.hi.hiui.refresh.HiRefreshLayout
import com.sum.hi.hiui.refresh.HiTextOverView
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiAdapter
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.hiitem.HiRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/04 20:46
 * @类描述 ${TODO}
 */
open class HiAbsFragment : HiBaseFragment(), HiRefresh.HiRefreshListener {
    private var pageIndex: Int = 1
    private lateinit var hiAdapter: HiAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var refreshHeadView: HiTextOverView
    private var contentLoading: ContentLoadingProgressBar? = null
    private var emptyView: EmptyView? = null
    private var recyclerView: HiRecyclerView? = null
    private var refreshLayout: HiRefreshLayout? = null

    companion object {
        const val PRFETCH_SIZE = 5
    }

    override fun getLayoutId(): Int = R.layout.fragment_list

    /**
     * Fragment 在ViewPage中，左右滑动的时候Fragment是会被回收的，它被回收的时候，Fragment对象不会被销毁，
     * 只不过它的view对象会被销毁，滑回来的时候它的生命周期是会重新执行的
     */
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.refreshLayout = refresh_layout
        this.recyclerView = recycler_view
        this.emptyView = empty_view
        this.contentLoading = content_loading

        refreshHeadView = HiTextOverView(requireContext())
        refreshLayout?.setRefreshOverView(refreshHeadView)
        refreshLayout?.setRefreshListener(this)
        layoutManager = createLayoutManager()

        hiAdapter = HiAdapter(requireContext())

        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = hiAdapter

        emptyView?.let {
            it.visibility = View.GONE
            it.setIcon(R.string.list_empty)
            it.setDesc(getString(R.string.list_empty_desc))
            it.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                onRefresh()
            })
        }

        pageIndex = 1
        contentLoading?.visibility = View.VISIBLE
    }

    fun finishRefresh(dataItem: List<HiDataItem<*, RecyclerView.ViewHolder>>?) {
        val success = !dataItem.isNullOrEmpty()
        val refresh = pageIndex == 1
        if (refresh) {
            contentLoading?.visibility = View.GONE
            refreshLayout?.refreshFinished()
            if (success) {
                emptyView?.visibility = View.GONE
                hiAdapter.clearItems()
                hiAdapter.addItems(dataItem!!, true)
            } else {
                //需要判断列表上是否有数据，如果没有则显示空视图
                if (hiAdapter?.itemCount <= 0) {
                    emptyView?.visibility = View.VISIBLE
                }

            }
        } else {
            if (success) {
                hiAdapter.addItems(dataItem!!, true)
            } else {

            }
            recyclerView?.loadFinished(true)
        }
    }

    fun enableLoadMore(callback: () -> Unit) {
        //能这么直接写吗？
//        recyclerView?.enableLoadMore(callback, PRFETCH_SIZE)
        //为了防止同时下拉刷新和上拉分页，这里需要处理
        recyclerView?.enableLoadMore({
            if (refreshHeadView.state == HiOverView.HiRefreshState.STATE_REFRESH) {
                //正处于刷新状态
                recyclerView?.loadFinished(true)
                return@enableLoadMore
            }
            pageIndex++
            callback()
        }, PRFETCH_SIZE)
    }

    fun disableLoadMore() {
        recyclerView?.disableLoadMore()
    }

    open fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext())
    }

    @CallSuper//会被子类复写，为了不让子类覆盖这里的逻辑
    override fun onRefresh() {
        if (recyclerView?.isLoadingMore() == true) {
            //正处于分页
            refreshLayout?.refreshFinished()
            return
        }
        pageIndex == 1
    }

    override fun enableRefresh(): Boolean {
        return true
    }
}