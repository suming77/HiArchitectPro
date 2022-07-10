package com.sum.hi.ui.biz.account

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.*
import com.sum.hi.hilibrary.util.AppGlobals
import com.sum.hi.hilibrary.util.SpUtils
import com.sum.hi.ui.biz.LoginActivity
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/11 00:09
 * @类描述 ${TODO}
 */
object AccountManager {
    private var boardingPass: String? = null
    private val KEY_BOARDING_PASS = "boarding_pass"
    private val loginLiveData = MutableLiveData<Boolean>()
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            //需要手动移除
            loginLiveData.observeForever(observer)
            loginForeverObservers.add(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            //否则高版本会报错
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (context == null) {
            throw IllegalStateException("context must not be null")
        }

        context.startActivity(intent)
    }

    fun loadingSuccess(boardingPass: String) {
        this.boardingPass = boardingPass
        SpUtils.putString(KEY_BOARDING_PASS, boardingPass)
        loginLiveData.postValue(true)
        clearLoginForeverObserver()
    }

    private fun clearLoginForeverObserver() {
        loginForeverObservers.forEach {
            loginLiveData.removeObserver(it)
        }
        loginForeverObservers.clear()
    }

    fun getBoardingPass(): String? {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SpUtils.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }
}