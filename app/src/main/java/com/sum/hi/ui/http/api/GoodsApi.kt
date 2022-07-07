package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.*
import org.devio.`as`.proj.main.model.GoodsList

/**
 * @author tea
 * @date   2022/7/7 14:20
 * @desc
 */
interface GoodsApi {
    @GET("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Filed("subcategoryId") subcategoryId: String,
        @Filed("pageSize") pageSize: Int,
        @Filed("pageIndex") pageIndex: Int
    ): HiCall<GoodsList>
}