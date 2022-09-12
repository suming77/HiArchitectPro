package com.example.service.login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.public_mod.model.UserProfile

////1.将这个接口打包成.jar对外提供
//public interface ILoginService {
//    void login();
//}
////2.实现接口中的方法，并按照下图方式注册
//public class LoginServiceImpl implements ILoginService{
//
//    @Override
//    public void login() {
//        //……
//    }
//}
////3.使用时根据接口类名查询具体的实现类
//public static <S> ServiceLoader<S> load(Class<S> service)

/**
 * 只是定义了一个接口，提供了对外相关能力，其他模块只需要按需添加，需要在biz_login模块实现
 */
interface ILoginService{
    fun login(context: Context?, observer :Observer<Boolean>)

    fun isLogin():Boolean

    fun getUserInfo(lifecycleOwner: LifecycleOwner?,
                    observer: Observer<UserProfile?>,
                    onlyCache: Boolean = false)
}
