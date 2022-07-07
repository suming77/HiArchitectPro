package com.sum.hi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sum.hi.common.component.HiBaseFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hiui.tab.top.HiTabTopInfo
import com.sum.hi.ui.R
import com.sum.hi.ui.home.HomeTabFragment
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.HomeApi
import kotlinx.android.synthetic.main.fragment_home.*
import org.devio.`as`.proj.main.model.TabCategory

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
class HomeFragment : HiBaseFragment() {
    private var topTabSelectIndex: Int = 0
    private val selectTabIndex: Int = 0
    private lateinit var tabCategoryList: MutableList<TabCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryTabList()
    }

    private fun queryTabList() {
        updateUI(null)
/*        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.code == HiResponse.SUCCESS && data != null) {
//                    updateUI(data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                }

            })*/
    }

    private fun updateUI(data: List<TabCategory>?) {
        //正在被移除，和容器解除关联，或者所在的activity已经被销毁了
        //viewModel+liveData就不需要这样判断了，它是和宿主的生命周期相关联
        if (isRemoving || isDetached || activity == null) {
            return
        }
        tabCategoryList = mutableListOf<TabCategory>()
        tabCategoryList.add(TabCategory("0", "热门", getGoodsCount()))
        tabCategoryList.add(TabCategory("1", "女装", getGoodsCount()))
        tabCategoryList.add(TabCategory("2", "鞋包", getGoodsCount()))
        tabCategoryList.add(TabCategory("3", "内衣", getGoodsCount()))
        tabCategoryList.add(TabCategory("4", "百货", getGoodsCount()))
        tabCategoryList.add(TabCategory("5", "手机", getGoodsCount()))
        tabCategoryList.add(TabCategory("6", "食品", getGoodsCount()))
        tabCategoryList.add(TabCategory("7", "男装", getGoodsCount()))
        tabCategoryList.add(TabCategory("8", "母婴", getGoodsCount()))
        tabCategoryList.add(TabCategory("9", "美妆", getGoodsCount()))
        tabCategoryList.add(TabCategory("10", "数码", getGoodsCount()))
        tabCategoryList.add(TabCategory("11", "生鲜", getGoodsCount()))

        val topTabs = mutableListOf<HiTabTopInfo<Int>>()
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.color_333)
        val selectColor = ContextCompat.getColor(requireContext(), R.color.color_dd2)
        //快速拿到元素和下标
        tabCategoryList.forEachIndexed { index, tabCategory ->
            val topTabInfo = HiTabTopInfo<Int>(tabCategory.categoryName, defaultColor, selectColor)
            topTabs.add(topTabInfo)
        }

        val topTabLayout = tab_top_layout
        val viewPager = view_pager
        topTabLayout.inflateInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[selectTabIndex])

        topTabLayout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            //点击选中下标
            if (viewPager.currentItem != index) {
                viewPager.setCurrentItem(index, false)//不需要平滑效果
            }
        }

        viewPager.adapter = HomePagerAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            tabCategoryList
        )

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //这个方法被触发有两种可能，1切换tab，2手动滑动页面
                //如果是手动滑动分页, 去通知topTabLayout切换
                if (position != topTabSelectIndex) {
                    topTabLayout.defaultSelected(topTabs[position])
                    topTabSelectIndex = position
                }

            }
        })
    }

    inner class HomePagerAdapter(
        fragmentManager: FragmentManager,
        behavior: Int,
        val tabs: List<TabCategory>
    ) : FragmentPagerAdapter(fragmentManager, behavior) {
        val fragments = SparseArray<Fragment>(tabs.size)
        override fun getCount(): Int {
            return tabs.size
        }

        override fun getItem(position: Int): Fragment {
            var fragment = fragments.get(position, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(position, fragment)
            }
            return fragment
        }

    }

    var goodsCount = 5

    fun getGoodsCount(): String {
        goodsCount++
        return goodsCount.toString()
    }

    private fun navigationView(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "HomeFragment -- onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "HomeFragment -- onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "HomeFragment -- onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "HomeFragment -- onStop: ")
    }
}