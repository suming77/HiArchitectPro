package com.sum.hi.ui.biz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.TextUtils
import com.sum.hi.common.view.EmptyView
import com.sum.hi.ui.R
import kotlinx.android.synthetic.main.activity_degrade_global.*
import java.net.URI

/**
 * @Author:   smy
 * @Date:     2022/2/19 15:07
 * @Desc: 全局统一错误页
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {

    //ARouter自动完成映射, Autowired自动装箱，必须使用Arouter.getInstance.inject(this)
    //需要添加@JvmField，因为ARouter会认为degrade_title是一个私有的字段，无法完成编译工作
    @JvmField
    @Autowired
    var degrade_title: String? = null
    @JvmField
    @Autowired
    var degrade_desc: String? = null
    @JvmField
    @Autowired
    var degrade_action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //这样才可以在activity的intent当中读取数据，给他们赋值
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_degrade_global)
        val emptyView = findViewById<EmptyView>(R.id.empty_view)
        if (!TextUtils.isEmpty(degrade_title)) {
            emptyView.setTitle(degrade_title!!)
        }
        if (!TextUtils.isEmpty(degrade_desc)) {
            emptyView.setDesc(degrade_desc!!)
        }

        if (degrade_action != null) {
            emptyView.setHelpAction(listener = object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(degrade_action))
                    startActivity(intent)
                }
            })
        }
        action_back.setOnClickListener{
            onBackPressed()
        }
    }
}