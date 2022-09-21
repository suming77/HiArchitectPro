package com.sum.hi.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sum.hi.common.http.ApiFactory
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.ui.model.GoodsModel

/**
 * @author smy
 * @date   2022/9/19 12:11
 * @desc
 */
class SearchViewModel : ViewModel() {
     var pageIndex = 1

    //不存在下拉刷新
    companion object {
        const val pageSize = 10
    }

    fun quickSearch(keyWord: String): LiveData<List<KeyWord>> {
        val liveData = MutableLiveData<List<KeyWord>>()
        ApiFactory.create(SearchApi::class.java).quickSearch(keyWord)
            .enqueue(object : HiCallback<QuickSearchList> {
                override fun onSuccess(response: HiResponse<QuickSearchList>) {
                    liveData.postValue(response.data?.list)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }

            })
        return liveData
    }

    fun saveHistory(keyWord: KeyWord) {

    }

    val goodsSearchLiveData = MutableLiveData<List<GoodsModel>>()

    /**
     * 不能像上面一样没饿次创建一次LiveData
     */
    fun goodsSearch(keyWord: String) {
        ApiFactory.create(SearchApi::class.java).goodsSearch(keyWord, pageIndex, pageSize)
            .enqueue(object : HiCallback<GoodsListSearch> {
                override fun onSuccess(response: HiResponse<GoodsListSearch>) {
                    goodsSearchLiveData.postValue(response.data?.list)
                    pageIndex++
                }

                override fun onFailed(throwable: Throwable) {
                    goodsSearchLiveData.postValue(null)
                }

            })
    }
}