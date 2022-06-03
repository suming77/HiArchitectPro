package com.sum.hi.ui.biz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.common.component.HiBaseActivity
import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.util.SpUtils
import com.sum.hi.ui.R
import com.sum.hi.ui.http.AccountApi
import com.sum.hi.ui.http.ApiFactory
import kotlinx.android.synthetic.main.activity_login.*

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
        action_login.setOnClickListener {
            goLogin()
        }

    }

    private fun goLogin() {
        val name = input_item_user_name.getEditText().text
        val password = input_item_password.getEditText().text

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
                        SpUtils.putString("boarding-pass", data!!)
                        setResult(Activity.RESULT_OK, Intent())
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
        ARouter.getInstance().build("/account/regsiter").navigation(this, 101)
//        startActivityForResult(Intent(this, RegisterActivity::class.java), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null)) {
            val userName = data!!.getStringExtra("username")
            if (!TextUtils.isEmpty(userName)) {
                input_item_user_name.getEditText().setText(userName)
            }
        }
    }
}