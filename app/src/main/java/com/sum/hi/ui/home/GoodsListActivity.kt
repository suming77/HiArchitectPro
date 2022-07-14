package com.sum.hi.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.ui.R
import kotlinx.android.synthetic.main.actiivty_goods_list.*

/**
 * @author tea
 * @date   2022/7/7 12:26
 * @desc
 */
@Route(path = "/goods/list")
class GoodsListActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var categoryTitle: String? = null

    @JvmField
    @Autowired
    var subCategoryId: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""
    private val FRAGMENT_TAG = "GOODS_LIST_FRAGMENT"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actiivty_goods_list)
        ARouter.getInstance().inject(this)

        action_back.setOnClickListener { onBackPressed() }
        category_title.text = categoryTitle

        //防止Fragment在Activity重启的时候造成重叠，所以在创建Fragment之前先到supportFragmentManager中查询一下
        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            fragment = GoodsListFragment.newInstance(categoryId, subCategoryId)
        }
        //判断Fragment是新创建出来的，不是恢复出来的
        val ft = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            ft.add(R.id.container, fragment, FRAGMENT_TAG)
        }
        ft.show(fragment).commitNowAllowingStateLoss()
    }
}