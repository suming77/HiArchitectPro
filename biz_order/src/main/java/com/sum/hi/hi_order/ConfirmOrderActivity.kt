package com.sum.hi.hi_order

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.sum.hi.common.view.loadUrl
import kotlinx.android.synthetic.main.activity_confirm_order.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/01 12:21
 * @类描述 ${TODO}
 */
class ConfirmOrderActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        initView()
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        shopLogo?.apply { shop_logo.loadUrl(this) }
        shop_title.text = shopName
        goodsImage?.apply { goods_image.loadUrl(this) }
        goods_name.text = goodsName
        goods_price.text = "$ $goodsPrice 件"
    }
}