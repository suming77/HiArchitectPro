package com.sum.hi.ui.home

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hiui.banner.HiBanner
import com.sum.hi.hiui.banner.core.HiBannerMo
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.route.HiRouter
import com.sum.hi.ui.model.HomeBanner

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/05 23:58
 * @类描述 ${TODO}
 */
class BannerItem(val list: List<HomeBanner>) :
    HiDataItem<List<HomeBanner>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val banner = holder.itemView as HiBanner
        val models = mutableListOf<HiBannerMo>()
        list.forEachIndexed { index, homeBanner ->
            val bannerMo = object : HiBannerMo() {}
            bannerMo.url = homeBanner.cover
            models.add(bannerMo)
        }
        banner.setBannerData(models)

        banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].url))
//            viewHolder.rootView.context.startActivity(intent)
            HiRouter.startActivity4Browser(list[position].url)
        }

        banner.setBindAdapter { viewHolder, mo, position ->
            ((viewHolder.rootView) as ImageView).loadUrl(mo.url)
        }
        //ViewTree已经被计算好了，开始去绘制，这个时间点所有的view都已经测量完成，并且给出一个具体的frame宽高的值，
        //接着就可以去绘制了，一旦绘制完成就可见可交互了，有人会有疑问为什么不选择view绘制完成之后呢？
        //里面也提供了一个onDrawListener()的回调，但是这两个方法的时间点是差不多的。
//        banner.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                return false
//            }
//        })
    }

    override fun getItemView(parent: ViewGroup): View? {
        val context = parent.context
        val banner = HiBanner(context)
        val layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            HiDisplayUtil.dp2px(160f, context.resources)
        )
        layoutParams.bottomMargin = HiDisplayUtil.dp2px(10f, context.resources)
        banner.layoutParams = layoutParams
        banner.setBackgroundColor(Color.WHITE)
        return banner
    }
}