package com.sum.hi.hi_hilt

import android.content.Context
import android.widget.Toast
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/28 23:35
 * @类描述 ${TODO}Hilt
 */
@Module
@InstallIn(ApplicationComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindService(impl: LoginServiceImpl): ILoginService

    /**
     * Provides不需要在接口传递具体的实现类型，但是需要在body完成方法的具体实现，无法获取到context,这里使用Binds
     */
//    @Provides
//    fun bindService(context): ILoginService {
//        return LoginServiceImpl(context)
//    }
}

interface ILoginService {
    fun login()
}

//构造函数需要标记 @Inject 才可以识别出构造函数的参数是被动态填充的
class LoginServiceImpl @Inject constructor(@ApplicationContext val context: Context) :
    ILoginService {
    override fun login() {
        Toast.makeText(context, "ILoginServiceImpl#login", Toast.LENGTH_LONG).show()
    }
}