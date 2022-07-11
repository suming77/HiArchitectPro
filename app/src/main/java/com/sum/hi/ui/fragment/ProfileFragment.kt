package com.sum.hi.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.common.component.HiBaseFragment
import com.sum.hi.common.view.loadCorner
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hiui.banner.HiBannerAdapter
import com.sum.hi.hiui.banner.core.HiBannerMo
import com.sum.hi.ui.R
import com.sum.hi.ui.biz.account.AccountManager
import com.sum.hi.ui.http.api.AccountApi
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.model.UserProfile
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
class ProfileFragment : HiBaseFragment() {
    private val REQUEST_CODE_LOGIN_PROFILE = 1001
    val ITEM_PLACE_HOLDE = "    "
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        item_course.setText(R.string.if_notify)
        item_course.append(ITEM_PLACE_HOLDE + getString(R.string.item_notify))

        item_collection.setText(R.string.if_collection)
        item_collection.append(ITEM_PLACE_HOLDE + getString(R.string.item_collection))

        item_adress.setText(R.string.if_address)
        item_adress.append(ITEM_PLACE_HOLDE + getString(R.string.item_address))

        item_history.setText(R.string.if_history)
        item_history.append(ITEM_PLACE_HOLDE + getString(R.string.item_history))

        user_name.setOnClickListener {
//            ARouter.getInstance().build("/account/login")
//                .navigation(activity, REQUEST_CODE_LOGIN_PROFILE)
            AccountManager.login(context, Observer {
                queryLoginUserInfo()
            })
        }

        queryLoginUserInfo()
        //模拟数据
        updateUI()
    }

    private fun queryLoginUserInfo() {
//        ApiFactory.create(AccountApi::class.java).profile()
//            .enqueue(object : HiCallback<UserProfile> {
//                override fun onSuccess(response: HiResponse<UserProfile>) {
//                    val data = response.data
//                    if (response.code == HiResponse.SUCCESS && data != null) {
//                        updateUI()
//                    }
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    showToast(throwable.message)
//                }
//
//            })
        AccountManager.getUserProfile(lifecycleOwner = this, observer = Observer { profie ->
            if (profie != null) {
                updateUI()
            } else {
                showToast("用户信息获取失败")
            }
        }, onlyCache = false)
    }

    private fun updateUI() {
        user_name.text = "刘亦菲"
        login_desc.text = "嗨，欢迎回来"
        tab_item_collection.text =
            spannableTabItem("10", getString(R.string.profile_tab_item_collection))
        tab_item_history.text = spannableTabItem("20", getString(R.string.profile_tab_item_history))
        tab_item_learn.text = spannableTabItem("30", getString(R.string.profile_tab_item_learn))

        updateBanner()
    }

    private fun updateBanner() {
        var modles = mutableListOf<HiBannerMo>()
        for (i in 1..10) {
            val hiBannerMo = object : HiBannerMo() {}//抽象类
            hiBannerMo.url = "www.baidu.com"
            modles.add(hiBannerMo)
        }
        hi_banner.setBannerData(R.layout.banner_item_layout, modles)
        hi_banner.setBindAdapter { viewHolder: HiBannerAdapter.HiBannerViewHolder?, mo: HiBannerMo?, position: Int ->
            if (viewHolder == null && mo != null) return@setBindAdapter
            val banner_item_imageview =
                viewHolder?.findViewById<ImageView>(R.id.banner_item_imageview)
            banner_item_imageview?.loadCorner(mo!!.url, HiDisplayUtil.dp2px(10f, resources))
        }
        hi_banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bannerMo.url))
            startActivity(intent)
        }
        hi_banner.isVisible = true
    }

    private fun spannableTabItem(topText: String, bottomText: String): CharSequence? {
        val ssb = SpannableStringBuilder()
        val ssTop = SpannableString(topText)
        ssTop.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_000
                )
            ), 0, ssTop.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ssTop.setSpan(AbsoluteSizeSpan(18, true), 0, ssTop.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        ssTop.setSpan(StyleSpan(Typeface.BOLD), 0, ssTop.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        ssb.append(ssTop)
        ssb.append(bottomText)
        return ssb.toString()
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "ProfileFragment -- onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "ProfileFragment -- onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "ProfileFragment -- onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "ProfileFragment -- onStop: ")
    }

    /**
     * 因为是使用Arouter启动的，它在启动的时候并没有使用Fragmegnt的startActivityForResult,
     * 而是使用了Activity启动的，那么Fragment的onActivityResult就回不来了，而需要复写Activity的onActvityResult方法
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN_PROFILE && resultCode == Activity.RESULT_OK && data != null) {
            queryLoginUserInfo()
        }
    }
}