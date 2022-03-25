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
    var parameters: MutableMap<String, Any>? = null
    var doMainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null

    //表明METHOD注解的类型
    @IntDef(value = [METHOD.GET, METHOD.POST])
    internal annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }


}