package com.sum.hi.hilibrary.restful

import android.text.TextUtils
import androidx.annotation.IntDef
import com.sum.hi.hilibrary.annotation.CacheStrategy
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.net.URLEncoder

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:34
 * @Desc:
 */
class HiRequest {
    private var cacheStrategyKey: String = ""
    var cacheStrategy: Int = CacheStrategy.NET_ONLY

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

    fun getCacheKey(): String {

        if (!TextUtils.isEmpty(cacheStrategyKey)) {
            return cacheStrategyKey
        }
        val builder = StringBuilder()
        val endUrl = endPointUrl()
        if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0) {
            builder.append("&")
        } else {
            builder.append("?")
        }

        if (parameters != null) {
            for ((key, value) in parameters!!) {
                try {
                    val encodeValue = URLEncoder.encode(value, "UTF-8")
                    builder.append(key).append("=").append(encodeValue).append("&")
                } catch (e: Exception) {
                    //ignore
                }
            }
            builder.deleteCharAt(builder.length - 1)
            cacheStrategyKey = builder.toString()
        } else {
            cacheStrategyKey = endUrl
        }
        return cacheStrategyKey
    }


}