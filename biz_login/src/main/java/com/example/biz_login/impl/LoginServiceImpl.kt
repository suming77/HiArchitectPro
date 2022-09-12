package com.example.biz_login.impl

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.example.biz_login.AccountManager
import com.example.public_mod.model.UserProfile
import com.example.service.login.ILoginService

/**
 * @author smy
 * @date   2022/9/9 23:15
 * @desc 按照Arouter的规则还需要实现IProvider,还需要在类上面标记@Route注解
 */
@Route(path = "/login/service")
class LoginServiceImpl : ILoginService, IProvider {
    override fun login(context: Context?, observer: Observer<Boolean>) {
        AccountManager.login(context, observer)

        //有了这个实现类之后就可以在外部模块需要实现的地方，来获取对外提供服务的能力
        //但是这样可能每个模块都需要这么写，最好包装在serviceLogin模块下面，任意模块就可以通过
        // LoginServiceProvider使用iLoginService使用对外提供暴露的能力嘞
//        val loginService = ARouter.getInstance().build("/login/service").navigation() as ILoginService
//        loginService.isLogin()
    }

    override fun isLogin(): Boolean {
        return AccountManager.isLogin()
    }

    override fun getUserInfo(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean
    ) {
        AccountManager.getUserProfile(lifecycleOwner, observer, onlyCache)
    }

    override fun init(context: Context?) {
        //暂无实现
    }
}