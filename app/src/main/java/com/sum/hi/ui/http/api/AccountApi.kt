package com.sum.hi.ui.http.api

import com.sum.hi.hilibrary.annotation.Filed
import com.sum.hi.hilibrary.annotation.GET
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.POST
import com.sum.hi.ui.model.UserProfile


/**
 * @创建者 mingyan.su
 * @创建时间 2022/05/29 10:36
 * @类描述 ${TODO}
 */

interface AccountApi {
    @POST("/user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): HiCall<String>

    @POST("/user/registration")
    fun register(
        @Filed("userName") userName: String,
        @Filed("password") password: String,
        @Filed("imoocId") imoocId: String,
        @Filed("orderId") orderId: String
    ): HiCall<Any?>

    @GET("/user/profile")
    fun profile(): HiCall<UserProfile>
}