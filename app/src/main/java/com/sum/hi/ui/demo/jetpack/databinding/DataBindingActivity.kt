package com.sum.hi.ui.demo.jetpack.databinding

import android.app.Activity
import android.app.AppComponentFactory
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author smy
 * @date   2022/7/26 14:19
 * @desc
 */
class DataBindingActivity : AppCompatActivity() {
    val textName = TextView(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            val result = requestUserInfo()
            textName.text = result
        }
    }
}

suspend fun requestUserInfo(): String {
    delay(2 * 1000)
    return "result from request"
}

//算术运算符：+ - * / %
//字符串链接运算符：+
//逻辑运算符：&& ||
//二元运算符：& | ^
//一元运算符：+ — ! ~
//移位运算符：>> >>> <<
//比较运算符：== > < >= <=(<需要被转义成 &lt; >需要被转义为 &gt;)
//instanceof
//分组运算符：()
//字面量运算符：字符，字符串，数字，null
//类型转换，方法调用
//字段访问，数组访问
//三元运算符：？
//不支持 this，super，new，显示泛型调用
