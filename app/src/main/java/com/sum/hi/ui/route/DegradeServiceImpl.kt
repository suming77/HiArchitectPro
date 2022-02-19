package com.sum.hi.ui.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @Author:   smy
 * @Date:     2022/2/19 15:00
 * @Desc:
 */
@Route(path = "/degrade/global/service")
class DegradeServiceImpl : DegradeService {
    override fun init(context: Context?) {

    }

    override fun onLost(context: Context?, postcard: Postcard?) {
        //绿色通道跳转到错误页
        ARouter.getInstance().build("/degrade/global/activity").greenChannel().navigation()
    }
}