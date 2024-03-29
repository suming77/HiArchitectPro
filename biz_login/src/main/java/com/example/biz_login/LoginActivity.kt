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
 * @创建时间 2022/05/29 10:20
 * @类描述 ${TODO}
 */
@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionRegister = findViewById<TextView>(R.id.action_register)
        actionRegister.setOnClickListener {
            Log.e("smy", "action_register")
            goRegistration()
        }
        findViewById<TextView>(R.id.action_login).setOnClickListener {
            goLogin()
        }

    }

    private fun goLogin() {
        val name = findViewById<InputItemLayout>(R.id.input_item_user_name).getEditText().text
        val password = findViewById<InputItemLayout>(R.id.input_item_password).getEditText().text

        if (TextUtils.isEmpty(name) or TextUtils.isEmpty(password)) {
            return
        }

        ApiFactory.create(AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.code == HiResponse.SUCCESS) {
                        showToast("登录成功")
                        val data = response.data

                        //UserManager来管理用户信息的读取
//                        SpUtils.putString("boarding-pass", data!!)
                        AccountManager.loadingSuccess(data!!)
                        setResult(RESULT_OK, Intent())
                        finish()
                    } else {
                        showToast("登录失败${response.msg}")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast("登录失败${throwable.message}")
                }

            })
    }

    private fun goRegistration() {
//        ARouter.getInstance().build("/account/regsiter").navigation(this, 101)
    //        路由更多是用于模块之间的路由跳转，而模块内依旧可以使用原生的方式
        startActivityForResult(Intent(this, RegisterActivity::class.java), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null)) {
            val userName = data!!.getStringExtra("username")
            if (!TextUtils.isEmpty(userName)) {
                findViewById<InputItemLayout>(R.id.input_item_user_name).getEditText().setText(userName)
            }
        }
    }
}