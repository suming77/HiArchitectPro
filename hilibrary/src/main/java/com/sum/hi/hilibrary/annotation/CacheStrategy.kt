package com.sum.hi.hilibrary.annotation

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/10 15:23
 * @类描述 ${TODO}
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class CacheStrategy(val value: Int = 1) {
    companion object {
        const val CACHE_FIRST = 0//请求接口的时候先读取本地缓存，再读取接口，接口成功后更新缓存(页面初始化数据)
        const val NET_ONLY = 1//仅仅请求接口(一般是分页和独立列表页)
        const val NET_CACHE = 2//先接口，接口成功后更新缓存(一般是下拉刷新)
    }
}
