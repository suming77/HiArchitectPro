package com.sum.hi.hilibrary.restful

import androidx.annotation.IntDef
import java.lang.reflect.Type

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:34
 * @Desc:
 */
class HiRequest {
    //只能填METHOD中则值，而不是随便填
    @METHOD
    var httpMethod: Int = 0
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String>? = null
    var doMainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null
    var formPost: Boolean = true

    //表明METHOD注解的类型
    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }

    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must not be null")
        }
        if (!relativeUrl!!.startsWith("/")) {
            return doMainUrl + relativeUrl
        }

        val indexOf = doMainUrl!!.indexOf("/")
        return doMainUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }

        headers!![name] = value
    }


}