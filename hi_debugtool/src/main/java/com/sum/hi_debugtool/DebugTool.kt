package com.sum.hi_debugtool

import android.content.Intent
import android.os.Process
import com.sum.hi.hilibrary.fps.FpsMonitor
import com.sum.hi.hilibrary.util.AppGlobals
import com.sum.hi.hilibrary.util.SpUtils

/**
 * @author smy
 * @date   2022/7/7 17:24
 * @desc
 *
 * 1.http无法抓包问题
 * 2.线上环境来回切换问题
 * 3.有没有查看app构建信息的需求
 * 4.有没有快速清理缓存的需求
 *
 * 1。https默认情况不能抓包，如果需要强行抓包也是可以的
 * Https如何降级成http
 * 需要服务端更改配置文件，支持http 80端口监听，
 * 如果服务端基于nginx来开发，需要在server配置文件当中开启http 80 端口的监听，可以了
 * server{
 *      listen 80 default backlog = 2048
 *      listen 443 ssl
 * }
 *
 * 如果服务端使用的是split或者http2.0的协议呢？也是在服务中开启相应的监听就可以了
 *
 * 2。Charles抓包原理
 *
 */
class DebugTool {
    fun buildVersion(): String {
//        return "构建版本：${BuildConfig.VERSION}.${BuildConfig.VERSION_CODE}"
        return "构建版本：1.0.1.0"
    }

    fun buildTime(): String {
        //不能直接在这里new Data()这个表示当前运行的时间，而我们需要的是包打出来的时间
        return "构建时间：${BuildConfig.BUILD_TIME}"
    }

    fun buildEnvironment(): String {
        //测试环境，正式环境
        return "构建环境：${BuildConfig.BUILD_TYPE}"
    }

    @HiDebug(name = "一键开启Https降级", desc = "将继承Http,可以使用抓包工具文明抓包")
    fun degrade2Http() {
        SpUtils.putBoolean("degrade2Http", true)
        //重新指定域名，只能通过重启
        val context = AppGlobals.get() ?: return
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

    @HiDebug(name = "查看crash日志", desc = "可以一键分享给开发同学，迅速定位问题")
    fun crashLog() {
        val context = AppGlobals.get()?.applicationContext ?: return
        //找到当前启动项是哪一项
        val intent = Intent(context, CrashLogActivity::class.java)
        //因为是通过Application启动的，需要添加flag:FLAG_ACTIVITY_NEW_TASK
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @HiDebug(name = "打开/关闭Fps", desc = "打开后可以实时查看页面的Fps")
    fun toggleFps(){
        FpsMonitor.toggle()
    }
}