package com.sum.hi.hilibrary.crash

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import android.text.format.Formatter
import com.sum.hi.hilibrary.BuildConfig
import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hilibrary.util.ActivityManager
import com.sum.hi.hilibrary.util.AppGlobals
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/11/27 16:19
 * @类描述 ${TODO}
 */
object CrashHandler {
    var CRASH_DIR = "crash_dir"
    fun init(javaCrashDir: String) {
        this.CRASH_DIR = javaCrashDir
        /**
         * 传递一个异常处理器,线程发生的异常都会传递到这个处理器里面
         */
        Thread.setDefaultUncaughtExceptionHandler(CaughtExceptionHandler())
    }

    private class CaughtExceptionHandler : Thread.UncaughtExceptionHandler {
        private var context = AppGlobals.get()!!
        private val formater = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA)
        private var LAUNCH_TIME = formater.format(Date())
        private val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        override fun uncaughtException(t: Thread, e: Throwable) {
            //如果不能处理这个异常，则需要给默认的处理器处理
            if (handlerException(e) && defaultExceptionHandler != null) {
                defaultExceptionHandler.uncaughtException(t, e)
            }
            restartApp()
        }

        private fun restartApp() {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)//清空之前所有的activity
            context.startActivity(intent)

            //需要杀死当前进程
            //0表示正常退出，其他数值表示不正常退出
            Process.killProcess(Process.myPid())
            System.exit(10)
        }

        /**
         * 设备类型，OS版本，线程名，前后台，使用时长，App版本，升级渠道
         * CPU架构，内存信息，存储信息，permission权限
         */
        private fun handlerException(e: Throwable): Boolean {
            if (e == null) return false
            val log = collectDeviceInfo((e))
            if (BuildConfig.DEBUG) {
                HiLog.e(log)
            }
            saveCrashInfo2File(log)
            return true
        }

        private fun saveCrashInfo2File(log: String) {
            //文件存储路径
//            val crashDir = File(context.cacheDir, CRASH_DIR)
            val crashDir = File(CRASH_DIR)
            //如果不存在则创建文件目录
            if (!crashDir.exists()) {
                crashDir.mkdirs()
            }
            //创建文件存放内容
            val crashFile = File(crashDir, formater.format(Date()) + "-crash.txt")
            crashFile.createNewFile()
            val fos = FileOutputStream(crashFile)
            try {
                fos.write(log.toByteArray())
                fos.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fos.close()
            }
        }

        fun collectDeviceInfo(e: Throwable): String {
            val sb = StringBuilder()
            sb.append("brand=${Build.BRAND}\n")//xiaomi huawei
            sb.append("rom=${Build.MODEL}\n")//系统版本sm-G9550
            sb.append("OS=${Build.VERSION.RELEASE}\n")//9.0
            sb.append("sdk=${Build.VERSION.SDK_INT}\n")//9.0版本的sdk是28
            sb.append("launch_time=${LAUNCH_TIME}\n")//APP启动时间
            sb.append("crash_time=${formater.format(Date())}\n")//crash的时间
            sb.append("forground=${ActivityManager.instance.front}\n")//应用处于前台后台
            sb.append("Thread=${Thread.currentThread().name}\n")//线程名
            sb.append("cpu_arch=${Build.CPU_ABI}\n")//CPU架构 armv7 armv8

            //app 信息
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            sb.append("version_code=${packageInfo.versionCode}\n")//
            sb.append("version_name=${packageInfo.versionName}\n")//
            sb.append("packageName=${packageInfo.packageName}\n")//
            sb.append("requested_permission=${Arrays.toString(packageInfo.requestedPermissions)}\n")//已申请到那些权限

            //统计存储空间信息
            val memInfo = android.app.ActivityManager.MemoryInfo()
            val ams =
                context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            ams.getMemoryInfo(memInfo)
            sb.append("availMem=${Formatter.formatFileSize(context, memInfo.availMem)}\n")//可用内存
            sb.append("availTotalMem=${Formatter.formatFileSize(context, memInfo.totalMem)}\n")//总内存

            val file = Environment.getExternalStorageDirectory()
            val statFs = StatFs(file.path)
            val availableSize = statFs.availableBlocks * statFs.blockSize
            sb.append(
                "availStorage=${
                    Formatter.formatFileSize(
                        context,
                        availableSize.toLong()
                    )
                }\n"
            )//存储空间


            val write: Writer = StringWriter()
            val printWriter = PrintWriter(write)
            e.printStackTrace(printWriter)
            //错误信息包括多个调用链，需要递归调用
            var cause = e.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }

            printWriter.close()
            sb.append(write.toString())//
            return sb.toString()
        }
    }
}