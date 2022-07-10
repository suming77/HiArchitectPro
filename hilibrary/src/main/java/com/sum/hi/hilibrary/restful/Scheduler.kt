package com.sum.hi.hilibrary.restful

import com.sum.hi.hilibrary.annotation.CacheStrategy
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.cache.HiCacheManager
import com.sum.hi.hilibrary.executor.HiExecutor
import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hilibrary.util.MainHandler

/**
 * @Author:   smy
 * @Date:     2022/3/25 20:53
 * @Desc:代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(
    private val callFactory: HiCall.Factory,
    private val interceptors: MutableList<HiInterceptor>
) {
    fun newCall(newRequest: HiRequest): HiCall<*> {
        val newCall: HiCall<*> = callFactory.newCall(newRequest)
        return ProxyCall(newCall, newRequest)
    }

    internal inner class ProxyCall<T>(val delegate: HiCall<T>, val newRequest: HiRequest) :
        HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(newRequest, null)
            //同步请求一般不需要缓存策略
            if (newRequest.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                val readCache = readCache<T>(newRequest)
                if (readCache.data != null) {
                    return readCache
                }
            }
            val response = delegate.execute()
            saveCacheIfNeed(response)
            dispatchInterceptor(newRequest, response)
            return response
        }

        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(newRequest, null)
            if (newRequest.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                val cacheResponse = readCache<T>(newRequest)
                if (cacheResponse.data != null) {
                    //抛到主线程里面, 尽快到业务方
                    MainHandler.sendAtFrontOfQueue(runnable = Runnable {
                        callback.onSuccess(cacheResponse)
                    })
                    HiLog.d("enqueue cache:${newRequest.getCacheKey()}")
                }
            }
            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(newRequest, response)
                    saveCacheIfNeed(response)
                    if (callback != null) callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    if (callback != null) callback.onFailed(throwable)
                }
            })
        }

        private fun saveCacheIfNeed(response: HiResponse<T>) {

            if (newRequest.cacheStrategy == CacheStrategy.CACHE_FIRST || newRequest.cacheStrategy == CacheStrategy.NET_CACHE) {
                if (response.data != null) {
                    HiExecutor.execute(runnable = Runnable {
                        HiCacheManager.saveCacheInfo(newRequest.getCacheKey(), response.data)
                    })
                }
            }
        }

        private fun <T> readCache(newRequest: HiRequest): HiResponse<T> {
            //CacheHomeManager查询缓存，需要提供一个cacheKey
            //方式1：request的URl增加一个参数
            //方式2：CacheStrategy增加一个cacheKey参数
            val cacheKey = newRequest.getCacheKey()
            val cacheBody = HiCacheManager.getCacheBody<T>(cacheKey)
            val cacheResponse = HiResponse<T>()
            cacheResponse.data = cacheBody
            cacheResponse.code = HiResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存获取成功"
            return cacheResponse
        }

        private fun dispatchInterceptor(newRequest: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(newRequest, response).dispatch()
        }

        internal inner class InterceptorChain(
            val request: HiRequest,
            val response: HiResponse<T>?
        ) : HiInterceptor.China {
            //代表的是分发的第几个拦截器
            var callIndex: Int = 0
            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HiRequest {
                return request
            }

            override fun response(): HiResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor: HiInterceptor = interceptors[callIndex]
                val intercept: Boolean = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }

        }
    }

}