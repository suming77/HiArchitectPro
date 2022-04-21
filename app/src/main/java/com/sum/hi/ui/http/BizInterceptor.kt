package com.sum.hi.ui.http

import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hilibrary.restful.HiInterceptor

/**
 * @Author:   smy
 * @Date:     2022/3/30 0:44
 * @Desc:
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(china: HiInterceptor.China): Boolean {
        if (china.isRequestPeriod) {//在请求发起的阶段
            val request = china.request()
            request.addHeader("auth-token", "fd82d1e882462e23b8e88aa82198f197")
        } else if (china.response() != null) {
            HiLog.dt("BizInterceptor", china.request().endPointUrl())
            HiLog.dt("BizInterceptor", china.response()!!.data)
        }
        return false
    }
}