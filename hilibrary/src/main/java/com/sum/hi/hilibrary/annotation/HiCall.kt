package com.sum.hi.hilibrary.annotation

import com.sum.hi.hilibrary.restful.HiRequest
import java.io.IOException

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:29
 * @Desc:
 */
interface HiCall<T> {
    //同步执行有可能抛出异常
    @Throws(IOException::class)
    fun execute(): HiResponse<T>

    //异步方法
    fun enqueue(callback: HiCallback<T>)

    interface Factory {
        fun newCall(request: HiRequest): HiCall<*>
    }
}