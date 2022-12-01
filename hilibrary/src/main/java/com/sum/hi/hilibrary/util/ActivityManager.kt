package com.sum.hi.hilibrary.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

class ActivityManager private constructor() {
    private val mActivityRefs = ArrayList<WeakReference<Activity>>()//弱引用，防止内存泄漏
    private val mFrontbackCallback = ArrayList<FrontBackCallback>()
    private var activityStartCount = 0
     var front = true


    //单例，线程安全模式
    companion object {
        val instance: ActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(innerActivityLifecycleCallbacks())
    }

    interface FrontBackCallback {
        fun onChange(font: Boolean)
    }

    fun addFrontBackCallback(callback: FrontBackCallback) {
        mFrontbackCallback.add(callback)
    }

    fun removeFrontBackCallback(callback: FrontBackCallback) {
        mFrontbackCallback.remove(callback)
    }

    inner class innerActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            mActivityRefs.add(WeakReference(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount++
            //activityStartCount > 0说明应用处于可见状态，也就是前台
            //!front之前是不是在后台
            if (!front && activityStartCount > 0) {
                front = true
                onFrontBackChange(front)
            }
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount--
            if (activityStartCount <= 0 && front) {
                front = false
                onFrontBackChange(false)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            for (activityRef in mActivityRefs) {
                if (activityRef != null && activityRef.get() == activity) {
                    mActivityRefs.remove(activityRef)
                }
            }
        }

    }

    private fun onFrontBackChange(front: Boolean) {
        for (frontBackCallback in mFrontbackCallback) {
            frontBackCallback.onChange(front)
        }
    }

    val topActivity: Activity?
        get() {
            if (mActivityRefs.size <= 0) {
                return null
            } else {
                mActivityRefs[mActivityRefs.size - 1].get()
            }
            return null
        }
}