package com.sum.hi.ui.route

import android.content.Intent
import android.net.Uri
import com.sum.hi.hilibrary.util.AppGlobals

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/06 00:33
 * @类描述 ${TODO}
 */
object HiRouter {
    fun startActivity4Browser(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        //目的是为了防止部分记性拉不起浏览器，比如华为
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        //目的是为了使用application的context启动Activity不会报错
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppGlobals.get()?.startActivity(intent)
    }
}