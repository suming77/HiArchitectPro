package com.sum.hi.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.ui.fragment.HiAbsFragment
import com.sum.hi.ui.model.GoodsList
import com.sum.hi.ui.model.GoodsModel

/**
 * @author tea
 * @date   2022/7/7 13:57
 * @desc
 */
class GoodsListFragment : HiAbsFragment() {
    @JvmField
    @Autowired
    var subCategoryId: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    companion object {
        fun newInstance(categoryId: String, subCategoryId: String): GoodsListFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("subCategoryId", subCategoryId)
            val fragment = GoodsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ARouter.getInstance().inject(this)
        loadData()
        enableLoadMore { loadData() }
    }

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        onQueryCategoryGoodsList(null)
//        ApiFactory.create(GoodsApi::class.java).queryCategoryGoodsList(
//            categoryId = categoryId,
//            subcategoryId = subCategoryId,
//            pageSize = 20,
//            pageIndex = pageIndex
//        ).enqueue(object : HiCallback<GoodsList> {
//            override fun onSuccess(response: HiResponse<GoodsList>) {
//                if (response.successful && response.data != null) {
//                    onQueryCategoryGoodsList(response.data!!)
//                } else {
//                    finishRefresh(null)
//                }
//            }
//
//            override fun onFailed(throwable: Throwable) {
//                finishRefresh(null)
//            }
//        })
    }

    private fun onQueryCategoryGoodsList(data: GoodsList?) {
        val url1 =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091119%2F3yu4knoncz13yu4knoncz1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659680201&t=e1b1570fddb12162c791dcc011c51eb8"
        val dataItems = mutableListOf<GoodsItem>()
//        for (goodsModel in data.list) {
//            val goodsItem = GoodsItem(goodsModel, false)
//            dataItems.add(goodsItem)
//        }
        for (i in 0..20) {
            dataItems.add(
                GoodsItem(
                    GoodsModel(
                        i.toString(),
                        "7天无理由 满200减50",
                        "0",
                        i.toString(),
                        "商品名称",
                        "99.99",
                        false,
                        null,
                        "98.98",
                        url1,
                        null,
                        "标记"
                    ), false
                )
            )
        }

        finishRefresh(dataItems)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(requireContext(), 2)
    }
}