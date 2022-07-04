package com.sum.hi.ui.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonObject
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.ui.R
import com.sum.hi.ui.demo.coroutine.CoroutineSense3
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.TestApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainDemoActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_demo)

        findViewById<TextView>(R.id.tv_hi_log).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_2).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_3).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_4).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_5).setOnClickListener(this)

        //        recyclerView.setLayoutManager();

//        ThreadDemoActivity.testLooperThread();

        //如果在java中调用ApiFactory，则需要ApiFactory.INSTANCE，因为在java文件中访问ApiFactory的create()方法的时候，需要调用INSTANCE
        ApiFactory.create(TestApi::class.java).listCity("imooc")
            .enqueue(object : HiCallback<JsonObject> {
                override fun onSuccess(response: HiResponse<JsonObject>) {
                    Log.e("BizInterceptor", "onSuccess 111== "+response.errorData)
                    Log.e("BizInterceptor", "onSuccess == "+response.code)
                    Log.e("BizInterceptor", "onSuccess == "+response.data)
                    Log.e("BizInterceptor", "onSuccess == "+response.msg)
                    Log.e("BizInterceptor", "onSuccess == "+response.rawData)
                }

                override fun onFailed(throwable: Throwable) {
                    Log.e("BizInterceptor", "onFailed == "+throwable.message)
                }

            })


//        HiDataBus.with<String>("StickyData").setStickData("StickyData from MainDemoActivity")
//        val viewModel = ViewModelProvider(this).get(ViewModelDemo.HiViewModel::class.java)
//        viewModel.loadInitData().observe(this, Observer {
//            //接收到数据
//        })
    }

    private suspend fun request1(): String {
        val result2 = request2()
        return "result from request1 + $result2"
    }

    private suspend fun request2(): String {
        delay(2000)
        return "result from request2"
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_hi_log -> {
//                val call: Continuation<*> =  Continuation<String>(Main) {result ->
//                    Log.e("smy",result.getOrNull()+"");
//                }
//                CoroutineDecompiled2.request1(call)
//                startActivity(Intent(this, HiLogDemoActivity::class.java))

                //由lifecycleScope启动的协程的生命周期与宿主的生命周期相关联，为什么相关联呢，
                // 协程也会发生内存泄漏的协程的泄漏本质上是线程的泄漏，宿主被销毁了，协程也会被停止

                lifecycleScope.launch {
                    val result = CoroutineSense3.parseAssetsFile(assets, "destination.json")
                    Log.e("smy", "result == $result")
                }
                Log.e("smy", "Coroutine == afterclick")
/*
                //是指当我们宿主的生命周期至少为onCreate的时候才会启动
                lifecycleScope.launchWhenCreated {
                    whenCreated {
                        //这里的代码只有宿舍的生命周期为onCreate才会执行，其他都是暂停
                    }

                    whenResumed {
                        //这里的代码只有宿舍的生命周期为onResume才会执行，其他都是暂停
                    }

                    whenStarted {
                        //这里的代码只有宿舍的生命周期为onStarted才会执行，其他都是暂停
                    }
                }

                //是指我们宿主生命周期至少为onStart的时候才会启动
                lifecycleScope.launchWhenStarted {

                }

                lifecycleScope.launchWhenResumed {

                }*/

                //全局生命周期
//                GlobalScope.launch {
//
//                }
            }
            R.id.tv_2 -> {
                startActivity(Intent(this, HiTabBottomDemoActivity::class.java))
            }
            R.id.tv_3 -> {
                startActivity(Intent(this, HiTabTopDemoActivity::class.java))
            }
            R.id.tv_4 -> {
                startActivity(Intent(this, HiRefreshDemoActivity::class.java))
            }
            R.id.tv_5 -> {
                startActivity(Intent(this, HiBannerDemoActivity::class.java))
            }
        }
    }

}