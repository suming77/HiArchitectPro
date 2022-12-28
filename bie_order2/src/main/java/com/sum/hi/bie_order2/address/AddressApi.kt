package com.sum.hi.bie_order2.address

import com.sum.hi.hilibrary.annotation.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/11 07:41
 * @类描述 ${TODO}
 */
interface AddressApi {
    @GET("address")
    fun queryAddress(
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<AddressModel>

    @POST("address", fromPost = false)
    fun addAddress(
        @Filed("province") province: String,
        @Filed("city") city: String,
        @Filed("area") area: String,
        @Filed("detail") detail: String,
        @Filed("receiver") receiver: String,
        @Filed("phoneNum") phoneNum: String
    ): HiCall<String>

    @PUT("address/{id}", formPost = false)
    fun updateAddress(
        @Path("id") id: String,
        @Filed("province") province: String,
        @Filed("city") city: String,
        @Filed("area") area: String,
        @Filed("detail") detail: String,
        @Filed("receiver") receiver: String,
        @Filed("phoneNum") phoneNum: String
    ): HiCall<String>

    @DELETE("address/{id}")
    fun deleteAddress(
        @Path("id") id: String
    ): HiCall<String>
}