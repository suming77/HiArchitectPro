package com.sum.hi.hilibrary.restful

import com.sum.hi.hilibrary.annotation.HiCall
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:58
 * @Desc:
 */
open class HiRestful constructor(val baseUrl: String, val callFactory: HiCall.Factory) {
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()
    private var scheduler: Scheduler
    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    init {
        scheduler = Scheduler(callFactory, interceptors)
    }

    //根据传进来的接口class对象，返回一个接口的代理对象
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->

            var methodParser = methodService[method]
            if (methodParser == null) {
                methodParser = MethodParser.parse(baseUrl, method, args)
                methodService.put(method, methodParser)
            }

            val newRequest = methodParser.newRequest()

//            callFactory.newCall(newRequest)
            scheduler.newCall(newRequest)
        } as T
    }
}