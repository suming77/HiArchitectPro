package com.sum.hi.hilibrary.annotation

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:23
 * @Desc: callback回调
 */
interface HiCallback<T> {
    fun onSuccess(response: HiResponse<T>)
    fun onFailed(throwable: Throwable)
}