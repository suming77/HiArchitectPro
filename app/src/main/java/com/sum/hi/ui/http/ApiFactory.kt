package com.sum.hi.ui.http

import com.sum.hi.hilibrary.restful.HiRestful
import com.sum.hi.hilibrary.util.SpUtils

/**
 * @Author:   smy
 * @Date:     2022/3/30 0:38
 * @Desc:
 */
object ApiFactory {

    private val HTTPS_BASE_URL = "https://api.devio.org/as/"
    private val HTTP_BASE_URL = "http://api.devio.org/as/"
    val degrade2Http = SpUtils.getBoolean("degrade2Http")
    val baseUrl = if (degrade2Http!!) HTTP_BASE_URL else HTTPS_BASE_URL
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpStatusInterceptor())

        //降级只在本次有效
        SpUtils.putBoolean("degrade2Http", false)
    }

    /**
     * 创建返回接口的代理对象
     */
    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}