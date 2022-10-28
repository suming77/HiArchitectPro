package com.sum.hi.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.common.component.HiBaseActivity
import com.sum.hi.common.view.EmptyView
import com.sum.hi.ui.HiStatusBar
import com.sum.hi.ui.R
import com.sum.hi.ui.TitleScrollListener
import com.sum.hi.ui.biz.account.AccountManager
import com.sum.hi.ui.hiitem.HiAdapter
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.DetailModel
import com.sum.hi.ui.model.GoodsModel
import com.sum.hi.ui.model.getPrice
import com.sum.hi.ui.route.HiRouter
import com.sum.hi.ui.viewmodel.GoodsDetailViewModel
import kotlinx.android.synthetic.main.activity_goods_detail.*

/**
 * @author smy
 * @date   2022/7/12 11:05
 * @desc
 */
@Route(path = "/goods/detail")
class GoodsDetailActivity : HiBaseActivity() {
    private var emptyView: EmptyView? = null
    private lateinit var viewModel: GoodsDetailViewModel

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, translucent = true)
        HiRouter.inject(this)
//        assert(!TextUtils.isEmpty(goodsId)) { "goodsId must not be null" }
        setContentView(R.layout.activity_goods_detail)
        initView()
        Log.e("smy", "goodsModel ==$goodsModel")
        action_order.setOnClickListener {

            ARouter.getInstance().build("/confirm/order")
                .withString("goodsImage", goodsModel?.sliderImage)
                .withString("goodsName", goodsModel?.goodsName)
                .withString("shopLogo", goodsModel?.sliderImage)
                .withString("shopName", "良品铺子")
                .withString("goodsPrice", goodsModel?.groupPrice)
                .navigation()
        }
    }

    private fun initView() {
        action_back.setOnClickListener { onBackPressed() }
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = HiAdapter(this)
        recycler_view.addOnScrollListener(TitleScrollListener(callback = {
            title_bar.setBackgroundColor(it)
        }))
        preBindData()
        viewModel = GoodsDetailViewModel.get("1", this)
        viewModel.queryGoodsDetail().observe(this, Observer {
            Log.e("smy", "detailData == $it")
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })
    }

    private fun preBindData() {
        if (goodsModel == null) {
            return
        }

        //notify为false，不能刷新，因为布局尚未完成，否则会报错
        val hiAdapter = recycler_view.adapter as HiAdapter
        hiAdapter.addItem(
            0, GoodsDetailHeaderItem(
                goodsModel!!.sliderImages,
                getPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice!!),
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams = ConstraintLayout.LayoutParams(-1, -1)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryGoodsDetail()
            })
            //需要才加载
            root_container.addView(emptyView)
        }

        recycler_view.visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

    private fun bindData(detailModel: DetailModel) {
        recycler_view.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE

        val hiAdapter = recycler_view.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        //头部模块
        dataItems.add(
            GoodsDetailHeaderItem(
                detailModel.sliderImages,
                getPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        //评论模块
        dataItems.add(CommentItem(model = detailModel))

        //店铺
        dataItems.add(ShopItem(detailModel))
        //商品描述
        dataItems.add(GoodsAttrItem(detailModel))
        //图库
        detailModel.gallery?.forEach {
            dataItems.add(GoodsGalleryItem(it))
        }
        //相似商品
        detailModel.similarGoods?.let {
            dataItems.add(GoodsSimilarTitleItem())
            it.forEach {
                dataItems.add(GoodsItem(it, false))
            }
        }
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)

        updateFavoriteActionFace(detailModel.isFavorite)
        updateOrderAction(detailModel)
    }

    private fun updateOrderAction(detailModel: DetailModel) {
        action_order.text = getPrice(detailModel.groupPrice, detailModel.marketPrice) + "\n立即购买"
    }

    private fun updateFavoriteActionFace(favorite: Boolean) {
        action_favorite.setOnClickListener {
            toggleFavorite()
        }
        action_favorite.setTextColor(
            ContextCompat.getColor(
                this,
                if (favorite) R.color.color_dd2 else R.color.color_999
            )
        )
    }

    private fun toggleFavorite() {
        if (!AccountManager.isLogin()) {
            AccountManager.login(this, Observer { loginSuccess ->
                if (loginSuccess) {
                    toggleFavorite()
                }
            })
        } else {
            action_favorite.isClickable = false
            viewModel.toggleFavorite().observe(this, Observer { success ->
                action_favorite.isClickable = true
                if (success != null) {//网络成功
                    updateFavoriteActionFace(success)
                    val message = if (success) "收藏成功" else "取消收藏"
                    showToast(message)
                } else {//网络失败

                }
            })
        }

    }
}