package com.sum.hi.ui.demo.coroutine

import android.util.Log
import kotlinx.coroutines.delay

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/27 10:14
 * @类描述 ${TODO}
 */
object CoroutineScene2 {
    private val TAG = "CoroutineScene2"
    suspend fun request2(): String {
        delay(2000)
        Log.e(TAG, "request2 complete")
        return "result form request2"
    }
}