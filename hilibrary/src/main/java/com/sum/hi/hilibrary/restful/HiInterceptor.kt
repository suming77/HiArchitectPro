package com.sum.hi.hilibrary.restful

import com.sum.hi.hilibrary.annotation.HiResponse

/**
 * @Author:   smy
 * @Date:     2022/3/23 21:51
 * @Desc:
 */
interface HiInterceptor {
    //返回值代表要不要拦截
    fun intercept(china: China): Boolean

    /**
     * China 对象会在我们派发拦截器的时候创建
     */
    interface China {
        //是否正在request
        val isRequestPeriod: Boolean get() = false
        fun request(): HiRequest

        /**
         * response在网络发起是为空的
         */
        fun response(): HiResponse<*>?
    }
}