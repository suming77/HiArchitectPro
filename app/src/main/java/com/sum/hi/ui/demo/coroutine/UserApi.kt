package com.sum.hi.ui.demo.coroutine


import com.sum.hi.hilibrary.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @创建者 mingyan.su
 * @创建时间 2021/06/10 18:10
 * @类描述 ${TODO}网络请求api
 */
interface UserApi {

    @GET("users/{login}")
    fun getUserInfo(@Path("login") login: String): Call<User>

    @GET("users/{login}")
    suspend fun getUserSuspend(@Path("login") login: String): User

}