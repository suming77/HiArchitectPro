package com.example.service.login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.example.public_mod.model.UserProfile

/**
 * @author smy
 * @date   2022/9/9 23:24
 * @desc 那么任意模块就能通过LoginServiceProvider使用对外暴露的能力了
 */
object LoginServiceProvider {
    val iLoginService = ARouter.getInstance().build("/login/service").navigation() as? ILoginService

    fun login(context: Context?, observer: Observer<Boolean>) {
        iLoginService?.login(context, observer)
    }

    fun isLogin(): Boolean {
        return iLoginService?.isLogin() == true
    }

    fun getUserInfo(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = false
    ) {
        iLoginService?.getUserInfo(lifecycleOwner, observer, onlyCache)
    }
}