package com.sum.hi.ui.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.sum.hi.ui.R

class MainActivity2 : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<TextView>(R.id.tv_hi_log).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_2).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_3).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_hi_log -> {
                startActivity(Intent(this, HiLogDemoActivity::class.java))
            }
            R.id.tv_2 -> {
                startActivity(Intent(this, HiTabBottomDemoActivity::class.java))
            }  R.id.tv_3 -> {
                startActivity(Intent(this, HiTabTopDemoActivity::class.java))
            }
        }
    }

}