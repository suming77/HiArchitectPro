package com.example.biz_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_login.api.AccountApi
import com.sum.hi.common.component.HiBaseActivity
import com.sum.hi.common.http.ApiFactory
import com.sum.hi.common.view.InputItemLayout
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse

/**
 * @创建者 mingyan.su
 * @创建时间 2022/06/03 09:43
 * @类描述 ${TODO}
 */
@Route(path = "/account/regsiter")
class RegisterActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViewById<TextView>(R.id.tv_back).setOnClickListener {
            onBackPressed()
        }
        val actionRegister = findViewById<TextView>(R.id.action_register)
        actionRegister.setOnClickListener {
            onSubmit()
        }
    }

    private fun onSubmit() {
        val orderId = findViewById<InputItemLayout>(R.id.input_item_order_name).getEditText().text.toString()
        val moocId = findViewById<InputItemLayout>(R.id.input_item_moocid).getEditText().text.toString()
        val userName = findViewById<InputItemLayout>(R.id.input_item_username).getEditText().text.toString()
        val psw = findViewById<InputItemLayout>(R.id.input_item_psw).getEditText().text.toString()
        val pswSec = findViewById<InputItemLayout>(R.id.input_item_psw_confirm).getEditText().text.toString()

        if (TextUtils.isEmpty(orderId)
            or TextUtils.isEmpty(moocId)
            or TextUtils.isEmpty(userName)
            or TextUtils.isEmpty(psw)
            or TextUtils.isEmpty(pswSec)
            or !TextUtils.equals(psw, pswSec)
        ) {
            return
        }
        Log.e("smy", "onSubmit")
        ApiFactory.create(AccountApi::class.java).register(userName, psw, orderId, moocId)
            .enqueue(object : HiCallback<Any?> {
                override fun onSuccess(response: HiResponse<Any?>) {
                    showToast("注册成功")
                    val intent = Intent()
                    intent.putExtra("username", userName)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                override fun onFailed(throwable: Throwable) {
                    showToast("注册失败")
                }

            })
    }
}
