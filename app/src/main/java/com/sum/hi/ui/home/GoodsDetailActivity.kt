package com.sum.hi.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.sum.hi.common.component.HiBaseActivity
import com.sum.hi.common.view.EmptyView
import com.sum.hi.ui.HiStatusBar
import com.sum.hi.ui.R
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

        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, true)
        HiRouter.inject(this)
        assert(!TextUtils.isEmpty(goodsId)) { "goodsId must not be null" }
        setContentView(R.layout.activity_goods_detail)
        initView()
    }

    private fun initView() {
        action_back.setOnClickListener { onBackPressed() }
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = HiAdapter(this)
        preBindData()
        viewModel = GoodsDetailViewModel.get(goodsId, this)
        viewModel.queryGoodsDetail().observe(this, Observer {
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
        hiAdapter.addItem(0, GoodsDetailHeaderItem(
            goodsModel!!.sliderImages,
            getPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice),
            goodsModel!!.completedNumText,
            goodsModel!!.goodsName
        ), false)
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
        emptyView!!.visibility = View.GONE

        recycler_view.adapter = HiAdapter(this)
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        dataItems.add(
            GoodsDetailHeaderItem(
                detailModel.sliderImages,
                getPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
    }
}