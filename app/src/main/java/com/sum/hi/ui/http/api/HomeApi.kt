package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.*
import org.devio.`as`.proj.main.model.HomeModel
import org.devio.`as`.proj.main.model.Subcategory
import org.devio.`as`.proj.main.model.TabCategory
import retrofit2.http.Query

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/01 22:05
 * @类描述 ${TODO}
 */
interface HomeApi {
    //首页tab
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>

    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<HomeModel>

}