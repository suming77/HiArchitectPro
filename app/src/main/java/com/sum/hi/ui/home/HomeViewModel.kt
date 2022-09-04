package com.sum.hi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/03 17:04
 * @类描述 ${TODO}
 */
class HomeViewModel(private val savedState: SavedStateHandle) : ViewModel() {
    var goodsCount = 5
    private lateinit var tabCategoryList: MutableList<TabCategory>

    fun getGoodsCount(): String {
        goodsCount++
        return goodsCount.toString()
    }

    fun queryTabList(): MutableLiveData<List<TabCategory>> {
        val liveData = MutableLiveData<List<TabCategory>>()
//        val memCache = savedState.get<List<TabCategory>>("categoryTabs")
//        if (memCache != null) {
//            liveData.postValue(memCache)
//            return liveData
//        }
        tabCategoryList = mutableListOf<TabCategory>()
        tabCategoryList.add(TabCategory("0", "热门", getGoodsCount()))
        tabCategoryList.add(TabCategory("1", "女装", getGoodsCount()))
        tabCategoryList.add(TabCategory("2", "鞋包", getGoodsCount()))
        tabCategoryList.add(TabCategory("3", "内衣", getGoodsCount()))
        tabCategoryList.add(TabCategory("4", "百货", getGoodsCount()))
        tabCategoryList.add(TabCategory("5", "手机", getGoodsCount()))
        tabCategoryList.add(TabCategory("6", "食品", getGoodsCount()))
        tabCategoryList.add(TabCategory("7", "男装", getGoodsCount()))
        tabCategoryList.add(TabCategory("8", "母婴", getGoodsCount()))
        tabCategoryList.add(TabCategory("9", "美妆", getGoodsCount()))
        tabCategoryList.add(TabCategory("10", "数码", getGoodsCount()))
        tabCategoryList.add(TabCategory("11", "生鲜", getGoodsCount()))
        liveData.postValue(tabCategoryList)
        //当HomeFragment因内存不足被回收而导致的重建会再次触发queryTabList()，那么可以从savedState里面取出原来的数据
        savedState.set("categoryTabs", tabCategoryList)
        return liveData
    }

    fun queryTabCategoryList(categoryId: String): MutableLiveData<List<HiDataItem<*, *>>> {
        val liveData = MutableLiveData<List<HiDataItem<*, *>>>()
//        val memCacheList = savedState.get<List<HiDataItem<*, *>>>("TabCategoryList")
//        if (memCacheList != null) {
//            liveData.postValue(memCacheList)
//            return liveData
//        }
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
                    ), categoryId == HomeTabFragment.DEFAULT_TOP_TAB_CRATEGORY_ID
                )
            )
        }

        liveData.postValue(dataItmes)
        return liveData
    }
}