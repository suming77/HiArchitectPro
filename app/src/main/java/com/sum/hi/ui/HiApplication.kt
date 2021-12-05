package com.sum.hi.ui

import android.app.Application
import com.google.gson.Gson
import com.sum.hi.common.component.HiBaseApplication
import com.sum.hi.hilibrary.log.HiConsolePrinter
import com.sum.hi.hilibrary.log.HiLogConfig
import com.sum.hi.hilibrary.log.HiLogManager
import com.sum.hi.hilibrary.log.HiLogPrinter

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 16:51
 * @类描述 ${TODO}
 */
class HiApplication : HiBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object : HiLogConfig() {
            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }

            override fun getGlobalTag(): String {
                return "Application"
            }

            override fun enable(): Boolean {
                return true
            }
        }, HiConsolePrinter())

    }
}