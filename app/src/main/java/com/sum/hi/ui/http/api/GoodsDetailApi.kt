package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.*
import com.sum.hi.ui.model.DetailModel
import com.sum.hi.ui.model.Favorite

/**
 * @author smy
 * @date 2022/7/12 11:06
 * @desc
 */
interface GoodsDetailApi {
    @GET("goods/detail/{id}")
    fun queryGoodsDetail(@Path("id") goodsId: String): HiCall<DetailModel>

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId") goodsId: String):HiCall<Favorite>
}