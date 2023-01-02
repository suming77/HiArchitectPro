package com.sum.hi.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.hilibrary.annotation.CacheStrategy
import com.sum.hi.hilibrary.cache.HiCacheManager
import com.sum.hi.ui.FoldableDeviceUtil
import com.sum.hi.ui.fragment.HiAbsFragment
import com.sum.hi.ui.hiitem.HiDataItem

/**
 * @author tea
 * @date   2022/7/5 17:56
 * @desc
 */
class HomeTabFragment : HiAbsFragment() {
    private var categoryId: String? = null
    lateinit var viewModel: HomeViewModel

    companion object {
        var DEFAULT_TOP_TAB_CRATEGORY_ID = "1"

        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_TOP_TAB_CRATEGORY_ID)
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        queryTabCategoryList(CacheStrategy.CACHE_FIRST)
        enableLoadMore { queryTabCategoryList(CacheStrategy.NET_ONLY) }

    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_TOP_TAB_CRATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(requireContext(), 2)
    }

    private fun queryTabCategoryList(cacheStrategy: Int) {
//        ApiFactory.create(HomeApi::class.java)
//            .queryTabCategoryList(categoryId!!, pageIndex = pageIndex, pageSize = 10)
//            .enqueue(object : HiCallback<HomeModel> {
//                override fun onSuccess(response: HiResponse<HomeModel>) {
//                    //不能把response.data!=null合并到response.successful里面，有可能会出现response.successful成功了但是data还是为空的情况
//                    if (response.successful && response.data != null) {
//                        updateUI(response.data!!)
//                    } else {
//                        finishRefresh(null)
//                    }
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    finishRefresh(null)
//                }
//            })

        viewModel.queryTabCategoryList(categoryId!!).observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

//    override fun onResume() {
//        super.onResume()
//        queryTabCategoryList(CacheStrategy.NET_CACHE)
//    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList(CacheStrategy.NET_CACHE)
    }

    private fun updateUI(data: List<HiDataItem<*, *>>?) {
        if (!isAlive) {
            return
        }

        Log.e("smy", "dataItmes == $data")
        finishRefresh(data!! as List<HiDataItem<*, RecyclerView.ViewHolder>>)
        HiCacheManager.saveCacheInfo("SubTabCategory", data)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (FoldableDeviceUtil.fold()) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
        } else {
            val gridLayoutManager = GridLayoutManager(context, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    //设置每行宽高比
                    return if (position <= 1) 2 else 1
                }
            }
            recyclerView?.layoutManager = gridLayoutManager
        }
    }
}