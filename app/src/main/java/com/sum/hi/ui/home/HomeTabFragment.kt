package com.sum.hi.ui.home

import android.os.Bundle
import android.view.View
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.ui.fragment.HiAbsFragment
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.HomeApi
import org.devio.`as`.proj.main.model.HomeModel

/**
 * @author tea
 * @date   2022/7/5 17:56
 * @desc
 */
class HomeTabFragment : HiAbsFragment() {
    private var categoryId: String? = null
    private var DEFAULT_TOP_TAB_CRATEGORY_ID = "1"

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = arguments?.getString("categoryId", DEFAULT_TOP_TAB_CRATEGORY_ID)
        queryTabCategoryList()
        enableLoadMore { queryTabCategoryList() }
    }

    private fun queryTabCategoryList() {
        ApiFactory.create(HomeApi::class.java).queryTabCategoryList(categoryId!!)
            .enqueue(object : HiCallback<HomeModel> {
                override fun onSuccess(response: HiResponse<HomeModel>) {
                    //不能把response.data!=null合并到response.successful里面，有可能会出现response.successful成功了但是data还是为空的情况
                    if (response.successful && response.data != null) {
                        updateUI(response.data!!)
                    } else {
                        finishRefresh(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    finishRefresh(null)
                }

            })
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList()
    }

    private fun updateUI(data: HomeModel) {
        if (!isAlive) {
            return
        }

        val dataItmes = mutableListOf<HiDataItem<*, *>>()
        data.bannerList?.let {
            dataItmes.add(BannerItem(it))
        }
        data.subcategoryList?.let {
            dataItmes.add(GridItem(it))
        }
        data.goodsList?.forEachIndexed { index, goodsModel ->
            dataItmes.add(GoodsItem(goodsModel))
        }
        finishRefresh(null)

    }
}