package com.sum.hi.common.http

import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.restful.HiInterceptor
import kotlin.math.log

/**
 * @创建者 mingyan.su
 * @创建时间 2022/06/03 12:07
 * @类描述 ${TODO}
 */
class HttpStatusInterceptor : HiInterceptor {
    override fun intercept(china: HiInterceptor.China): Boolean {
        val response = china.response()
        if (!china.isRequestPeriod && response != null) {
            val code = response.code
            Log.e("smy", " response.code == "+code)
            when (code) {
                HiResponse.RC_NEED_LOGIN -> {
                    ARouter.getInstance().build("/account/login").navigation()
                }
                //or 等于java的 | ，如果要|| 则用 ，
                HiResponse.RC_AUTH_TOKEN_EXPIRED,(HiResponse.RC_AUTH_TOKEN_INVALID),( HiResponse.RC_USER_FORBID) -> {
                    var helpUrl: String? = null
                    if (response.errorData != null) {
                        helpUrl = response.errorData!!.get("helpUrl")
                    }
                    ARouter.getInstance().build("/degrade/global/activity")
                        .withString("degrade_title", "非法访问")
                        .withString("degrade_desc", response.msg)
                        .withString("degrade_action", helpUrl)
                        .navigation()
                }
            }
        }
        return false
    }
}