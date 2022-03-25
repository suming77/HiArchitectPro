package com.sum.hi.hilibrary.annotation

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:20
 * @Desc:
 */
open class HiResponse<T> {
    companion object {
        val SUCCESS: Int = 0
    }

    var rawData: String? = null//原始数据
    var code = 0//业务状态码
    var data: T? = null//业务数据
    var errorData: Map<String, String>? = null
    var msg: String? = null//错误信息

}