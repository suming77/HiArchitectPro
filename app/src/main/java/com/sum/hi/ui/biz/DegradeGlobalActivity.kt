package com.sum.hi.ui.biz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.sum.hi.common.component.EmptyView
import com.sum.hi.ui.R

/**
 * @Author:   smy
 * @Date:     2022/2/19 15:07
 * @Desc:
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_degrade_global)
        val emptyView = findViewById<EmptyView>(R.id.emptyView)
        emptyView.setIcon(R.string.if_unexpected1)
        emptyView.setContent(getString(R.string.toast_error_degrade))
    }
}