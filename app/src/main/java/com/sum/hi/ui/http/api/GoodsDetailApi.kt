package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.GET
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.Path
import com.sum.hi.ui.model.DetailModel

/**
 * @author smy
 * @date 2022/7/12 11:06
 * @desc
 */
interface GoodsDetailApi {
    @GET("goods/detail/{id}")
    fun queryGoodsDetail(@Path("id") goodsId: String): HiCall<DetailModel>
}