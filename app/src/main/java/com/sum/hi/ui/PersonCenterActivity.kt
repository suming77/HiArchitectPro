package com.sum.hi.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author tea
 * @date   2022/7/5 16:44
 * @desc
 */
class PersonCenterActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_center)

        findViewById<TextView>(R.id.tv_profile)
            .setOnClickListener(View.OnClickListener { v: View? ->
                navigationView(
                    "/profile/detail"
                )
            })
        findViewById<TextView>(R.id.tv_authentication).setOnClickListener(
            View.OnClickListener { v: View? -> navigationView("/profile/authentication") })
        findViewById<TextView>(R.id.tv_vip).setOnClickListener(View.OnClickListener { v: View? ->
            navigationView(
                "/profile/vip"
            )
        })
        findViewById<TextView>(R.id.tv_unknow)
            .setOnClickListener(View.OnClickListener { v: View? ->
                navigationView(
                    "/profile/unknow"
                )
            })
    }

    private fun navigationView(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

}