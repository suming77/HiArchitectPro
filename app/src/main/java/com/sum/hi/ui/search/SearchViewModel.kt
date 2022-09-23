package com.sum.hi.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sum.hi.hilibrary.cache.HiCacheManager
import com.sum.hi.hilibrary.executor.HiExecutor
import com.sum.hi.ui.model.GoodsModel
import com.sum.hi.ui.model.SliderImage

/**
 * @author smy
 * @date   2022/9/19 12:11
 * @desc
 */
class SearchViewModel : ViewModel() {
    var cacheKeyWords: java.util.ArrayList<KeyWord>? = null
    var pageIndex = 1

    //不存在下拉刷新
    companion object {
        const val pageSize = 10
        const val SEARCH_HISTORY_KEY = "search_history"
        const val SEARCH_MAX_CACHE = 10
    }

    fun quickSearch(keyWord: String): LiveData<List<KeyWord>> {
        val liveData = MutableLiveData<List<KeyWord>>()
//        ApiFactory.create(SearchApi::class.java).quickSearch(keyWord)
//            .enqueue(object : HiCallback<QuickSearchList> {
//                override fun onSuccess(response: HiResponse<QuickSearchList>) {
//                    liveData.postValue(response.data?.list)
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    liveData.postValue(null)
//                }
//
//            })
        Log.e("smy", "quickSearch == ${keyWord}")
        val list = mutableListOf<KeyWord>()
        for (i in 0..10) {
            list.add(KeyWord(i.toString(), "iphone" + i))
        }
        Log.e("smy", "goodsSearch == ${list.size}")
//        saveLocalHistory(KeyWord(null, keyWord))
        liveData.postValue(list)
        return liveData
    }

    fun saveHistory(keyWord: KeyWord) {
        Log.e("smy", "saveHistorykeyWord == $keyWord")
        if (cacheKeyWords == null) {
            cacheKeyWords = ArrayList()
        }
        //如果存在则先移除在添加到首位
        cacheKeyWords?.apply {
            if (contains(keyWord)) {
                remove(keyWord)
            }
            add(0, keyWord)
            if (this.size > SEARCH_MAX_CACHE) {
                //超过最大值，则移除后面的
                dropLast(this.size - SEARCH_MAX_CACHE)
            }

            HiExecutor.execute(runnable = Runnable {
                Log.e("smy", "saveHistory == $cacheKeyWords")
                HiCacheManager.saveCacheInfo(SEARCH_HISTORY_KEY, cacheKeyWords)
            })
        }
    }

    val goodsSearchLiveData = MutableLiveData<List<GoodsModel>>()

    /**
     * 不能像上面一样没饿次创建一次LiveData
     */
    fun goodsSearch(keyWord: String?, loadInit: Boolean) {
        if (loadInit) pageIndex = 1
//        ApiFactory.create(SearchApi::class.java).goodsSearch(keyWord, pageIndex, pageSize)
//            .enqueue(object : HiCallback<GoodsListSearch> {
//                override fun onSuccess(response: HiResponse<GoodsListSearch>) {
        //这里不能使用postValue，会有延迟操作
//                   // goodsSearchLiveData.postValue(response.data?.list)
//                         goodsSearchLiveData.value = (response.data?.list)
//                    pageIndex++
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    goodsSearchLiveData.postValue(null)
//                }
//
//            })
        Log.e("smy", "goodsSearch == ${keyWord}")

        val url1 =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091119%2F3yu4knoncz13yu4knoncz1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659680201&t=e1b1570fddb12162c791dcc011c51eb8"
        val url =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242306111155-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275005&t=faeb269997b7402022a7d9f8bebdeee7"
        val sliderImages = mutableListOf<SliderImage>()
        val sliderImage1 = SliderImage(1, url)
        val sliderImage2 = SliderImage(2, url1)
        sliderImages.add(sliderImage1)
        sliderImages.add(sliderImage2)
        val head4 =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F80%2Fv2-c348c322c87c8cd7220297a14f3e39f3_720w.jpg%3Fsource%3D1940ef5c&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275495&t=a832484cc0028f1455030c54733c9592"
        val flowGoods = mutableListOf<GoodsModel>()
        val goods = GoodsModel(
            "1",
            "全场包邮 7天无理由 满200减50",
            "0",
            "0",
            "商品名称",
            "99.99",
            false,
            null,
            null,
            head4,
            sliderImages,
            "标记"
        )
        for (i in 0..10) {
            flowGoods.add(goods)
        }
        Log.e("smy", "goodsSearch == ${flowGoods.size}")
        goodsSearchLiveData.postValue(flowGoods)
    }


    fun queryLocalHistory(): LiveData<ArrayList<KeyWord>> {
        val liveData = MutableLiveData<ArrayList<KeyWord>>()
        HiExecutor.execute(runnable = Runnable {
            cacheKeyWords = HiCacheManager.getCacheBody<ArrayList<KeyWord>>(SEARCH_HISTORY_KEY)
            Log.e("smy", "cacheKeyWord == $cacheKeyWords")
            liveData.postValue(cacheKeyWords)
        })
        return liveData
    }

    fun clearHistory() {
        HiExecutor.execute(runnable = Runnable {
            HiCacheManager.deleteCacheInfo(SEARCH_HISTORY_KEY)
        })
    }
}