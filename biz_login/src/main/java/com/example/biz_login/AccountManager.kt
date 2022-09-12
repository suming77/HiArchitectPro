package com.example.biz_login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.*
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.cache.HiCacheManager
import com.sum.hi.hilibrary.executor.HiExecutor
import com.sum.hi.hilibrary.util.AppGlobals
import com.sum.hi.hilibrary.util.SpUtils
import com.sum.hi.ui.biz.LoginActivity
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.AccountApi
import com.sum.hi.ui.model.UserProfile
import java.lang.IllegalStateException

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/11 00:09
 * @类描述 ${TODO}
 */
object AccountManager {
    private var userProfile: UserProfile? = null

    @Volatile
    private var isFetching = false

    private var boardingPass: String? = null
    private val KEY_BOARDING_PASS = "boarding_pass"
    private val KEY_USER_PROFILE = "user_profile"
    private val loginLiveData = MutableLiveData<Boolean>()
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()

    private val userProfileLiveData = MutableLiveData<UserProfile?>()
    private val userProfileForeverObservers = mutableListOf<Observer<UserProfile?>>()

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
        loginLiveData.value = true
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

    /**
     * 有可能在子线程或者主线程，也有可能是同时发起的
     * 加同步锁
     */
    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean
    ) {
        if (lifecycleOwner == null) {
            userProfileLiveData.observeForever(observer)
            userProfileForeverObservers.add(observer)
        } else {
            userProfileLiveData.observe(lifecycleOwner, observer)
        }

        if (userProfile != null && onlyCache) {
            userProfileLiveData.postValue(userProfile)
            return
        }

        if (isFetching) {
            return
        }
        isFetching = true
        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    userProfile = response.data
                    if (response.code == HiResponse.SUCCESS && userProfile != null) {
                        HiExecutor.execute(runnable = Runnable {
                            HiCacheManager.saveCacheInfo(KEY_USER_PROFILE, userProfile)
                        })
                        //postValue本质是往主现场添加一条消息，具有一定的滞后性，可能clearProfileObservers被清除了才接收到数据
                        //这里不要使用postValue， 改用setValue
                        userProfileLiveData.value = userProfile
                    } else {
                        userProfileLiveData.value = null
                    }
                    clearProfileObservers()
                    isFetching = false
                }

                override fun onFailed(throwable: Throwable) {
                    userProfileLiveData.postValue(null)
                    clearProfileObservers()
                    isFetching = false
                }
            })
    }

    fun clearProfileObservers() {
        for (observer in userProfileForeverObservers) {
            userProfileLiveData.removeObserver(observer)
        }
        userProfileForeverObservers.clear()
    }

    /**
     * boardingPass不为空说明已经登录
     */
    fun isLogin(): Boolean {
        return TextUtils.isEmpty(boardingPass)
    }
}