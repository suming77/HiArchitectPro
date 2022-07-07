package com.sum.hi_debugtool

import android.content.Intent
import android.os.Process
import com.sum.hi.hilibrary.util.AppGlobals
import com.sum.hi.hilibrary.util.SpUtils

/**
 * @author smy
 * @date   2022/7/7 17:24
 * @desc
 */
class DebugTool {
    fun buildVersion(): String {
//        return "构建版本：${BuildConfig.VERSION}.${BuildConfig.VERSION_CODE}"
        return "构建版本：1.0.1."
    }

    fun buildTime(): String {
        //不能直接在这里new Data()这个表示当前运行的时间，而我们需要的是包打出来的时间
        return "${BuildConfig.BUILD_TIME}"
    }

    fun buildEnvironment(): String {
        //测试环境，正式环境
        return "构建环境:${BuildConfig.BUILD_TYPE}"
    }

    @HiDebug(name = "一键开启Https降级", desc = "将继承Http,可以使用抓包工具文明抓包")
    fun degrade2Http(){
        SpUtils.putBoolean("degrade2Http", true)
        //重新指定域名，只能通过重启
        val context = AppGlobals.get()?:return
        //找到当前启动项是哪一项
        val intent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        //找到启动页的Intent
        //因为是通过Application启动的，需要添加flag
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        //杀死当前线程，并主动启动新的启动页，完成重启的动作
        Process.killProcess(Process.myPid())
        return
    }
}