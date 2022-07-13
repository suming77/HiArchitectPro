package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.GET
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.Path
import com.sum.hi.ui.model.Subcategory
import com.sum.hi.ui.model.TabCategory

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/01 22:05
 * @类描述 ${TODO}
 */
interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>

    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}