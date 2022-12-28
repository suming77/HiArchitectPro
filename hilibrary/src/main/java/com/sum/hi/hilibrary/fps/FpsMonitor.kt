package com.sum.hi.hilibrary.fps

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.sum.hi.hilibrary.R
import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hilibrary.util.ActivityManager
import com.sum.hi.hilibrary.util.AppGlobals
import java.text.DecimalFormat

/**
 * @author mingyan.su
 * @date   2022/12/16 14:22
 * @desc
 */
object FpsMonitor {
    private val fpsViewer = FpsViewer()
    fun toggle() {
        fpsViewer.toggle()
    }

    fun listener(callback: FpsCallback) {
        fpsViewer.addListener(callback)
    }

    interface FpsCallback {
        fun onFrame(fps: Double)
    }

    private class FpsViewer {
        private var params = WindowManager.LayoutParams()
        private var isPlaying = false //进入后台后则停止
        private val application = AppGlobals.get()!!
        private val fpsView = LayoutInflater.from(application).inflate(R.layout.fpx_view, null, false) as TextView
        private val decimal = DecimalFormat("#.0 fps")
        private var windowManager: WindowManager? = null
        private var frameMonitor = FrameMonitor()

        init {
            windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            //不获取焦点不可点击不拦截事件
            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

            //透明窗体
            params.format = PixelFormat.TRANSLUCENT
            params.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
            //type设置得不对就会报verToken问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                params.type = WindowManager.LayoutParams.TYPE_TOAST
            }
            ActivityManager.instance.addFrontBackCallback(object : ActivityManager.FrontBackCallback {
                override fun onChange(font: Boolean) {
                    if (font) {
                        play()
                    } else {
                        stop()
                    }
                }
            })

            frameMonitor.addListener(object : FpsCallback {
                override fun onFrame(fps: Double) {
                    fpsView.text = decimal.format(fps)
                }
            })
        }

        fun toggle() {
            if (isPlaying) {
                stop()
            } else {
                play()
            }
        }

        private fun play() {
            if (!hasOverlayPermission()) {
                startOverlaySettingActivity()
                HiLog.e("app has no overlay permission")
                return
            }
            frameMonitor.start()
            if (!isPlaying) {
                isPlaying = true
                windowManager?.addView(fpsView, params)
            }

        }

        private fun startOverlaySettingActivity() {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                application.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + application.packageName)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

        private fun stop() {
            frameMonitor.stop()
            if (isPlaying) {
                isPlaying = false
                windowManager?.removeView(fpsView)
            }
        }

        /**
         * 是否有在顶部悬浮窗显示的权限
         */
        private fun hasOverlayPermission(): Boolean {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(application)
        }

        fun addListener(callback: FpsCallback) {
            frameMonitor.addListener(callback)
        }
    }

}

