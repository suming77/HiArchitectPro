package com.sum.hi.hilibrary.restful

import com.sum.hi.hilibrary.annotation.HiResponse
import java.lang.reflect.Type

/**
 * @Author:   smy
 * @Date:     2022/3/29 22:05
 * @Desc:
 */
interface HiConvert {
    fun <T> convert(rawDate: String, dataType: Type): HiResponse<T>
}