package com.sum.hi.ui.http

import com.sum.hi.hilibrary.restful.HiRestful

/**
 * @Author:   smy
 * @Date:     2022/3/30 0:38
 * @Desc:
 */
object ApiFactory {
    private val baseUrl = "https://api.devio.org/as/"
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpStatusInterceptor())
    }

    /**
     * 创建返回接口的代理对象
     */
    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}