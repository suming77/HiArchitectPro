package com.sum.hi.ui.route

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.sum.hi.hilibrary.util.MainHandler
import com.sum.hi.ui.biz.account.AccountManager
import java.lang.RuntimeException


/**
 * @Author:   smy
 * @Date:     2022/2/19 14:40
 * @Desc:
 */

/**
 * 添加注解，才能在编译的时候扫描到拦截器
 */
@Interceptor(name = "bi_interceptor", priority = 9)
class BizInterceptor : IInterceptor {
    private var mContext: Context? = null

    override fun init(context: Context?) {
        this.mContext = context
    }

    /**
     * 运行在ARouter的线程池当中，即在子线程中
     */
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra
        if ((flag and (RouterFlag.FLAG_LOGIN) != 0)) {
            //login
            callback?.onInterrupt(RuntimeException("need login"))
            showToast("请先登录")
            loginIntercept(postcard, callback)
        }/* else if ((flag and (RouterFlag.FLAG_AUTHENTICATION) != 0)) {
            callback?.onInterrupt(RuntimeException("need authentication"))
//            authentication()
            showToast("请先实名验证")
        } else if ((flag and (RouterFlag.FLAG_VIP) != 0)) {
            callback?.onInterrupt(RuntimeException("need become VIP"))
            showToast("需要成为会员")
        } */ else {
            /**
             * 路由继续执行
             */
            callback?.onContinue(postcard)
        }

    }

    /**
     * 转到主线程执行
     */
    private fun loginIntercept(postcard: Postcard, callback: InterceptorCallback?) {
//        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show()
//            ARouter.getInstance().build("/account/login")
//        }
        MainHandler.post(runnable = Runnable {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show()
            if (AccountManager.isLogin()) {
                callback?.onContinue(postcard)
            } else {
                AccountManager.login(mContext, Observer { success ->
                    callback?.onContinue(postcard)
                })
            }
        })
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }

    }
}