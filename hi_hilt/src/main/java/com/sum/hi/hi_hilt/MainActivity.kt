package com.sum.hi.hi_hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//标记@AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //标记@Inject
    @Inject
    lateinit var iLoginService: ILoginService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * 思考：1.ILoginService是如何被创建，如何被赋值的？
         *      2.LoginServiceImpl(context) 是如何被传递进去的？
         *      3.全局单例是如何实现的？
         *      把这三个掌握了，Hilt的运行时原理也就掌握了
         */
        iLoginService.login()
    }
}