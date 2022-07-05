package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.GET
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.Path
import org.devio.`as`.proj.main.model.HomeModel
import org.devio.`as`.proj.main.model.TabCategory

/**
 * @author tea
 * @date   2022/7/5 16:59
 * @desc
 */
interface HomeApi {
    //首页tab
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>

    @GET("home/{categoryId}")
    fun queryTabCategoryList(@Path("categoryId") categoryId: String): HiCall<HomeModel>
}