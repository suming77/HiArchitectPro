package com.sum.hi.common.http

import android.util.Log
import com.sum.hi.hilibrary.annotation.*
import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hilibrary.restful.HiRequest
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.lang.IllegalStateException

/**
 * @Author:   smy
 * @Date:     2022/3/29 13:23
 * @Desc:
 */
class RetrofitCallFactory(val baseUrl: String) : HiCall.Factory {
    private var gsonConvert: GsonConvert
    private var apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        gsonConvert = GsonConvert()
    }

    internal inner class RetrofitCall<T>(val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            val realCall = createRealCall(request)
            val response = realCall.execute()//执行execute()得到response
            return parseResponse(response)
        }

        override fun enqueue(callback: HiCallback<T>) {
            val realCall = createRealCall(request)
            realCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val parseResponse: HiResponse<T> = parseResponse(response)
                    callback.onSuccess(parseResponse)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailed(throwable = t)
                }

            })
        }

        private fun createRealCall(request: HiRequest): Call<ResponseBody> {
            if (request.httpMethod == HiRequest.METHOD.GET) {
                return apiService.get(request.headers, request.endPointUrl(), request.parameters)
            } else if (request.httpMethod == HiRequest.METHOD.POST) {
                var requestBody: RequestBody? = buildRequestBody(request)
                return apiService.post(request.headers, request.endPointUrl(), requestBody)
            } else if (request.httpMethod == HiRequest.METHOD.PUT) {
                var requestBody: RequestBody? = buildRequestBody(request)
                return apiService.put(request.headers, request.endPointUrl(), requestBody)
            } else if (request.httpMethod == HiRequest.METHOD.DELETE) {
                return apiService.delete(request.headers, request.endPointUrl())
            } else {
                throw  IllegalStateException("hirestful only support GET POST for now, url = " + request.endPointUrl())
            }
        }

        private fun parseResponse(response: Response<ResponseBody>): HiResponse<T> {
            var rawData: String? = null
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    HiLog.dt("BizInterceptor", "successbody == " + body.string())
                    rawData = body.string()
                }
            } else {
                val body = response.errorBody()
                if (body != null) {
//                    Log.e("BizInterceptor", "errorBody == "+body.string())
                    rawData = body.string()
//                    Log.e("BizInterceptor", "rawData == "+body.string())
                }
            }
            Log.e("BizInterceptor", "rawData == " + rawData)
            return gsonConvert.convert(rawData!!, request.returnType!!)
        }
    }

    private fun buildRequestBody(request: HiRequest): RequestBody? {
        val parameters = request.parameters
        val builder = FormBody.Builder()
        var requestBody: RequestBody? = null
        var jsonObject = JSONObject()
        for ((key, value) in parameters!!) {
            //如果是表单提交，通过frombody通过key value通过表单的形式提交上去
            if (request.formPost) {
                builder.add(key, value)
            } else {//非表单提交，parameters中的key value组装成json格式
                jsonObject.put(key, value)
            }
        }

        if (request.formPost) {
            requestBody = builder.build()
        } else {
            requestBody = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"),
                jsonObject.toString()
            )
        }
        return requestBody
    }


    override fun newCall(request: HiRequest): HiCall<Any> {
        return RetrofitCall(request)
    }

    interface ApiService {
        @retrofit2.http.GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @retrofit2.http.POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>

        @PUT
        fun put(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>

        @DELETE//不可携带requestBody
        fun delete(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String
        ): Call<ResponseBody>
    }
}