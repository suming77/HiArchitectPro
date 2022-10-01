package com.sum.hi.hi_hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/29 07:55
 * @类描述 ${TODO}
 */
@HiltAndroidApp
class HiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}