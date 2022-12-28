package com.sum.hi.hilibrary.fps

import android.view.Choreographer
import com.sum.hi.hilibrary.log.HiLog

/**
 * @author mingyan.su
 * @date   2022/12/16 15:57
 * @desc 每一帧都回调此方法
 */
internal class FrameMonitor : Choreographer.FrameCallback {
    private val choreographer = Choreographer.getInstance()
    private var frameStartTime: Long = 0 //这个是记录上一帧的时间戳
    private var frameCount = 0//一秒内确切绘制了多少帧

    private var listeners = arrayListOf<FpsMonitor.FpsCallback>()
    override fun doFrame(frameTimeNanos: Long) {
        val currentTimeMills = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(frameTimeNanos)
        if (frameStartTime > 0) {
            //计算两针之间的时间差
            //500ms 100ms
            val timeSpan = currentTimeMills - frameStartTime
            //fps每秒有多少帧 frame per second
            frameCount++
            if (timeSpan > 100) {
                val fps = frameCount * 1000 / timeSpan.toDouble()
                HiLog.e("FrameMonitor", fps)
                for (listener in listeners) {
                    listener.onFrame(fps)
                }
                frameCount = 0
                frameStartTime = currentTimeMills
            }
        } else {
            frameStartTime = currentTimeMills
        }
        start()//才能去监听下一帧的到达
    }

    fun start() {
        choreographer.postFrameCallback(this)
    }

    fun stop() {
        frameStartTime = 0
        listeners.clear()
        choreographer.removeFrameCallback(this)
    }

    fun addListener(callback: FpsMonitor.FpsCallback) {
        listeners.add(callback)
    }

}