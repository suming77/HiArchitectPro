package com.sum.hi.ui

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/06 21:47
 * @类描述 ${TODO}
 */
object HiStatusBar {

    /**
     * @param darkContent true:白底黑字
     * @param statusBarColor 状态栏背景色
     * @param translucent 沉浸式效果，页面布局延伸到状态栏里面
     */
    fun setStatusBar(
        activity: Activity,
        darkContent: Boolean,
        statusBarColor: Int = Color.WHITE,
        translucent: Boolean
    ) {

        //所有参数都是作用在window
        val window = activity.window
        //根布局
        val decorView = window.decorView
        //代表一些行为
        var visibility = decorView.systemUiVisibility

        //大于5.0才去改变状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //为了兼容不同的平台，不同的系统版本，让statusBarColor都能生效，需要为window增加一个flag
            //FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS请求系统绘制状态栏背景色，但是不能与FLAG_TRANSLUCENT_STATUS同时出现
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = statusBarColor
        }

        //设置浅色能力，需要6.0以上的能力
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            visibility = if (darkContent) {
                //白底黑字，浅色主题
                //需要给visibility增加SYSTEM_UI_FLAG_LIGHT_STATUS_BAR,能使状态栏字体颜色变黑色，背景变白色，同时更新visibility的值
                visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                //深色主题，黑体白字 需要除去SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                //java写法：visibility &=亦或 View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR，kotlin不支持这种写法
                visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }

        if (translucent) {
            // View.SYSTEM_UI_FLAG_FULLSCREEN：能使页面延伸到状态栏里面，但是状态栏的图标(信号，时间)也看不见了，即全屏模式
            //所以会搭配另外一个flag:SYSTEM_UI_FLAG_LAYOUT_STABLE恢复状态栏图标
            visibility =
                visibility or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        decorView.systemUiVisibility = visibility
    }
}