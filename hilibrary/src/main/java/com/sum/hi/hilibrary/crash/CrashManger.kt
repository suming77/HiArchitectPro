package com.sum.hi.hilibrary.crash

import com.sum.hi.hilibrary.util.AppGlobals
import java.io.File

/**
 * @创建者 mingyan.su
 * @创建时间 2022/11/30 08:34
 * @类描述 ${TODO}
 */
object CrashManger {
    private const val CRASH_DIR_JAVA = "crash_dir_java"
    private const val CRASH_DIR_NATIVE = "crash_dir_native"
    fun init() {
        val javaCrashDir = getJavaCrashDir()
        CrashHandler.init(javaCrashDir.absolutePath)
    }

    private fun getJavaCrashDir(): File {
        val javaCrashFile = File(AppGlobals.get()!!.cacheDir, CRASH_DIR_JAVA)
        if (!javaCrashFile.exists()) {
            javaCrashFile.mkdirs()
        }
        return javaCrashFile
    }

    private fun getNativeCrashDir(): File {
        val nativeCrashFile = File(AppGlobals.get()!!.cacheDir, CRASH_DIR_NATIVE)
        if (nativeCrashFile.exists()) {
            nativeCrashFile.mkdirs()
        }
        return nativeCrashFile
    }

    fun crashFiles(): Array<File> {
        return getJavaCrashDir().listFiles() + getNativeCrashDir().listFiles()
    }
}