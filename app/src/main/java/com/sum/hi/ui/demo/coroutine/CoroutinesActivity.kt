package com.sum.hi.ui.demo.coroutine

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/25 09:17
 * @类描述 ${TODO}
 */
class CoroutinesActivity :AppCompatActivity(){
    var textView: TextView = TextView(this)
    val userApi by lazy {
        val retrofit = retrofit2.Retrofit.Builder()
                .client(OkHttpClient().newBuilder().addInterceptor(
                        Interceptor {
                            it.proceed(it.request()).apply {
//                                log("request: ${code()}")
                            }
                        }
                ).build())
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(UserApi::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            val result = userApi.getUserSuspend("suming77")
            textView.text = result.name
        }
    }
}