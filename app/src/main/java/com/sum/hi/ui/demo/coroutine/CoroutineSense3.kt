package com.sum.hi.ui.demo.coroutine

import android.content.res.AssetManager
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * @Author:   smy
 * @Date:     2022/3/12 0:31
 * @Desc:以异步的方式读取assets目录下的文件，并且适配协程的写法，让他真正挂起函数
 * 方便调用，直接以同步的方法拿到返回值
 */
object CoroutineSense3 {
    suspend fun parseAssetsFile(assetManager: AssetManager, fileName: String): String {
        return suspendCancellableCoroutine { continuation ->
            //其他的都一样，开线程读取
            Thread(Runnable {
                val inputStream = assetManager.open(fileName)
                //逐行读取，不用逐个字节读取
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                val stringBuilder = StringBuilder()
                //kotlin中不允许这样写
//                while ((line = bufferedReader.readLine()) != null) {
//
//                }

                do {
                    line = bufferedReader.readLine()
                    if (line != null) stringBuilder.append(line) else break
                } while (true)

                inputStream.close()
                bufferedReader.close()

                Thread.sleep(2000)
                Log.e("smy", "Coroutine == onComplete")
                continuation.resumeWith(Result.success(stringBuilder.toString()))
            }).start()
        }
    }
}