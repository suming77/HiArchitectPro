package com.sum.hi.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.ui.fragment.HiAbsFragment
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.HomeApi
import org.devio.`as`.proj.main.model.GoodsModel
import org.devio.`as`.proj.main.model.HomeBanner
import org.devio.`as`.proj.main.model.HomeModel
import org.devio.`as`.proj.main.model.Subcategory

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
        categoryId = arguments?.getString("categoryId", DEFAULT_TOP_TAB_CRATEGORY_ID)
        Log.e("smy", "categoryId $categoryId")
        super.onViewCreated(view, savedInstanceState)
        queryTabCategoryList()
        enableLoadMore { queryTabCategoryList() }
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_TOP_TAB_CRATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(requireContext(), 2)
    }

    private fun queryTabCategoryList() {
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
//
//            })
        updateUI(null)
    }

    override fun onResume() {
        super.onResume()
        queryTabCategoryList()
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList()
    }

    private fun updateUI(data: HomeModel?) {
        if (!isAlive) {
            return
        }

        val dataItmes = mutableListOf<HiDataItem<*, *>>()
//        data.bannerList?.let {
//            dataItmes.add(BannerItem(it))
//        }
//        data.subcategoryList?.let {
//            dataItmes.add(GridItem(it))
//        }
//        data.goodsList?.forEachIndexed { index, goodsModel ->
//            dataItmes.add(GoodsItem(goodsModel, categoryId == DEFAULT_TOP_TAB_CRATEGORY_ID))
//        }

        val bannerList = mutableListOf<HomeBanner>()
        val url1 =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091119%2F3yu4knoncz13yu4knoncz1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659680201&t=e1b1570fddb12162c791dcc011c51eb8"
        for (i in 0..5) {
            bannerList.add(
                HomeBanner(
                    url1,
                    "0",
                    i.toString(),
                    0,
                    "二级标题",
                    "Banner标题",
                    "0", url1
                )
            )
        }
        dataItmes.add(BannerItem(bannerList))

        val subcategoryList = mutableListOf<Subcategory>()
        for (i in 0..9) {
            subcategoryList.add(
                Subcategory(
                    i.toString(),
                    "groupName",
                    "0",
                    url1,
                    i.toString(),
                    "限时秒杀$i"
                )
            )
        }
        dataItmes.add(GridItem(subcategoryList))

        for (i in 1..20) {
            dataItmes.add(
                GoodsItem(
                    GoodsModel(
                        i.toString(),
                        "全场包邮 7天无理由 满200减50",
                        "0",
                        i.toString(),
                        "商品名称",
                        "99.99",
                        true,
                        null,
                        "98.98",
                        url1,
                        null,
                        "标记"
                    ), categoryId == DEFAULT_TOP_TAB_CRATEGORY_ID
                )
            )
        }

        Log.e("smy", "dataItmes == $dataItmes")
        finishRefresh(dataItmes!! as List<HiDataItem<*, RecyclerView.ViewHolder>>)
    }
}