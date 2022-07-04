package com.sum.hi.ui.tab

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author tea
 * @date   2022/5/10 22:03
 * @desc
 */
abstract class BaseViewBindingActivity<T : ViewBinding> : AppCompatActivity() {

    protected val mViewBinding by lazy {
        //获取父类Type
        var superClass = javaClass.genericSuperclass
        if (superClass !is ParameterizedType) {
            superClass = (superClass as Class<*>).genericSuperclass
        }

        //如果支持泛型
        if (superClass is ParameterizedType) {
            //获得泛型中的第一个实际类型
            val type = (superClass as ParameterizedType).actualTypeArguments[0]

            //反射调用inflate
            val method = (type as Class<*>).getDeclaredMethod("inflate", LayoutInflater::class.java)
            //获得ViewBinding实例
            method.invoke(null, layoutInflater) as T
        } else {
            throw IllegalStateException(
                "This class must support generics and generic parameters must be viewbinding and its subclasses"
            )
        }
    }
}