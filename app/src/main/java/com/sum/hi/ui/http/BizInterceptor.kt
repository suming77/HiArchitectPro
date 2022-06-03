package com.sum.hi.ui.http

import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hilibrary.restful.HiInterceptor
import com.sum.hi.hilibrary.util.SpUtils

/**
 * @Author:   smy
 * @Date:     2022/3/30 0:44
 * @Desc:
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(china: HiInterceptor.China): Boolean {
        if (china.isRequestPeriod) {//在请求发起的阶段,添加全局header
            val request = china.request()
            //每次发起网络请求的时候都会带上这个字段
            val boarding_pass = SpUtils.getString("boarding-pass") ?: ""
            request.addHeader("boarding-pass", boarding_pass!!)
            request.addHeader("auth-token", "fd82d1e882462e23b8e88aa82198f197")
        } else if (china.response() != null) {
            HiLog.dt("BizInterceptor", china.request().endPointUrl())
            HiLog.dt("BizInterceptor", china.response()!!.rawData)
        }
        return false
    }
}