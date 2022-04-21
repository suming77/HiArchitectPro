package com.sum.hi.hilibrary.restful

import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse

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
            val response = delegate.execute()
            dispatchInterceptor(newRequest, response)
            return response
        }

        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(newRequest, null)
            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(newRequest, response)
                    if (callback != null) callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    if (callback != null) callback.onFailed(throwable)
                }
            })
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