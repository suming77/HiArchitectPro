package com.sum.hi.ui.search

import com.sum.hi.hilibrary.annotation.Filed
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.POST

/**
 * @author smy
 * @date   2022/9/19 12:12
 * @desc
 */
interface SearchApi {
    @com.sum.hi.hilibrary.annotation.GET("goods/search/quick")
    fun quickSearch(@Filed("keyWord") keyWord: String): HiCall<QuickSearchList>

    @POST("goods/search", fromPost = false)
    fun goodsSearch(
        @Filed("keyword") keyWord: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<GoodsListSearch>
}