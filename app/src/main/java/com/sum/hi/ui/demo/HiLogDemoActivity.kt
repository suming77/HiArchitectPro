package com.sum.hi.ui.demo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sum.hi.hilibrary.log.*
import com.sum.hi.ui.R

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 16:44
 * @类描述 ${TODO}
 */
class HiLogDemoActivity : AppCompatActivity() {
    var viewPainter: HiViewPrinter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        viewPainter = HiViewPrinter(this)
        findViewById<Button>(R.id.btn_hi_log).setOnClickListener {
            printLog();
        }
        viewPainter!!.printerProvider.showFloatingView()
    }

    private fun printLog() {
        HiLogManager.getInstance().addPrinter(viewPainter)
        //自定义Log配置
        HiLog.log(object : HiLogConfig() {
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }

        }, HiLogType.E, "-------", "5566")
        HiLog.a("9900")
    }


}