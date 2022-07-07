package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.GET
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.Path
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
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>

    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @Path("categoryId") categoryId: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): HiCall<HomeModel>

}