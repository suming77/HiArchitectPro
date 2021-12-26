package com.sum.hi.ui.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper

import androidx.appcompat.app.AppCompatActivity
import com.sum.hi.hiui.refresh.HiRefresh
import com.sum.hi.hiui.refresh.HiRefreshLayout
import com.sum.hi.hiui.refresh.HiTextOverView
import com.sum.hi.ui.R

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/08 00:44
 * @类描述 ${TODO}
 */
class HiRefreshDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh)
        val layout = findViewById<HiRefreshLayout>(R.id.refresh_layout)
        val hiOverView = HiTextOverView(this)
        layout.setRefreshOverView(hiOverView)
        layout.setRefreshListener(object : HiRefresh.HiRefreshListener {
            override fun enableRefresh(): Boolean {
                return true
            }

            override fun onRefresh() {
                Handler(Looper.getMainLooper()).postDelayed({ layout.refreshFinished() }, 1000)
            }

        })

//        refreshLayout.setDisableRefreshScroll(false)
    }
}