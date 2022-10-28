package com.sum.hi.hi_order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hi_order.address.Address
import kotlinx.android.synthetic.main.activity_confirm_order.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/01 12:21
 * @类描述 ${TODO}
 */
@Route(path = "/confirm/order")
class ConfirmOrderActivity : AppCompatActivity() {
    @JvmField
    @Autowired(name = "goodsImage")
    var goodsImage: String? = null

    @JvmField
    @Autowired(name = "goodsName")
    var goodsName: String? = null

    @JvmField
    @Autowired(name = "shopLogo")
    var shopLogo: String? = null

    @JvmField
    @Autowired(name = "shopName")
    var shopName: String? = null

    @JvmField
    @Autowired(name = "goodsPrice")
    var goodsPrice: String? = "99999"

    var viewModel: OrderViewModel? = null

    //拓展写法
//    private val viewModel by viewModels<OrderViewModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        initView()
        updateTotalPayPrice(amount_view.getAmountValue())
        viewModel?.queryMainAddress()?.observe(this, Observer {
            updateAddress(it)
        })
    }

    private fun updateAddress(address: Address?) {
        val hasMainAddress = address != null
        tv_add_address.visibility = if (hasMainAddress) View.GONE else View.VISIBLE
        rl_address.visibility = if (hasMainAddress) View.VISIBLE else View.GONE
        if (hasMainAddress) {
            user_name.text = address!!.receiver
            user_phone.text = address.phoneNum
            user_address.text = "${address.province} ${address.city} ${address.area}"
            rl_address.setOnClickListener {
                //地址列表页
                ARouter.getInstance().build("/address/list").navigation(this, 101)
            }
        } else {
            tv_add_address.setOnClickListener {
                //弹框新增地址
//                val dialog = AddNewAddressDialog.newInstance(null)
//                dialog.setOnSaveAddressListener(object : com.sum.hi.ui.address.AddNewAddressDialog.onSaveAddressListener {
//                    override fun onSaveAddress(address: Address?) {
//                        updateAddress(address)
//                    }
//                })
//                dialog.show(supportFragmentManager, "AddNewAddressDialog")
            }
        }
    }

    private fun initView() {
        shopLogo =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091119%2F3yu4knoncz13yu4knoncz1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659680201&t=e1b1570fddb12162c791dcc011c51eb8"
        goodsImage =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091119%2F3yu4knoncz13yu4knoncz1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659680201&t=e1b1570fddb12162c791dcc011c51eb8"
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        shopLogo?.apply { shop_logo.loadUrl(this) }
        shopName = "良品铺子"
        goodsName = "商品名称"
        shop_title.text = shopName
        goodsImage?.apply { goods_image.loadUrl(this) }
        goods_name.text = goodsName
        goods_price.text = "$ $99999 件"

        amount_view.setAmountValueChangeListener {
            updateTotalPayPrice(it)
        }

        rb_ali.setOnClickListener(channelPayListener)
        rb_wechat.setOnClickListener(channelPayListener)

        //支付sdk开发完成后才能支付
        order_buy.setOnClickListener {

        }
    }

    private fun updateTotalPayPrice(amount: Int) {
        //amount * goodsPrice.contains("￥")
        val price = PriceUtil.subPrice(goodsPrice)
        if (price != null) {
            goods_pay_total.text = String.format("实付款:￥%s 免运费", (amount * price.toFloat()))
        }
    }

    private val channelPayListener = View.OnClickListener {
        val aliPayChecked = rb_ali.id == R.id.rb_ali
        rb_ali.isChecked = aliPayChecked
        rb_wechat.isChecked = !aliPayChecked
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == 101) {
            val address = data.getSerializableExtra("result") as? Address?
            if (address != null) {
                updateAddress(address)
            }
        }
    }
}