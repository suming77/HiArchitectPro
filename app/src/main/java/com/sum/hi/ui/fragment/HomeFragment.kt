package com.sum.hi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sum.hi.common.component.HiBaseFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.sum.hi.hilibrary.cache.HiCacheManager
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hiui.search.HiSearchView
import com.sum.hi.hiui.tab.common.IHiTabLayout
import com.sum.hi.hiui.tab.top.HiTabTopInfo
import com.sum.hi.ui.R
import com.sum.hi.ui.home.HomeTabFragment
import com.sum.hi.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import com.sum.hi.ui.model.TabCategory

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
class HomeFragment : HiBaseFragment() {
    private var topTabSelectIndex: Int = 0
    private val selectTabIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.queryTabList().observe(viewLifecycleOwner, Observer {
            it?.let {
                updateUI(it)
            }
        })

        navigationBar.setNavListener(View.OnClickListener {
//            HiRouter.startActivity(requireContext(), null, "/search/main")
        })
        navigationBar.addRightTextButton("搜索", View.generateViewId())

        val searchView = HiSearchView(requireContext())
        searchView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(40f))
//        searchView?.postDelayed(Runnable {
//            searchView.setKeyWord("iphone", View.OnClickListener { })
//            ARouter.getInstance().build("/search/main").navigation()
//        }, 3000)
        searchView.setHintText("搜索你想要的商品")
        navigationBar.setContainerView(searchView)
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

    val onTabSelectListener =
        IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>> { index, prevInfo, nextInfo ->
            //点击选中下标
            if (view_pager.currentItem != index) {
                view_pager.setCurrentItem(index, false)//不需要平滑效果
            }
        }

    private fun updateUI(tabCategoryList: List<TabCategory>?) {
        //正在被移除，和容器解除关联，或者所在的activity已经被销毁了
        //viewModel+liveData就不需要这样判断了，它是和宿主的生命周期相关联
        if (isRemoving || isDetached || activity == null) {
            return
        }

        HiCacheManager.saveCacheInfo("TabCategory", tabCategoryList)

        val topTabs = mutableListOf<HiTabTopInfo<Int>>()
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.color_333)
        val selectColor = ContextCompat.getColor(requireContext(), R.color.color_dd2)
        //快速拿到元素和下标
        tabCategoryList?.forEachIndexed { index, tabCategory ->
            val topTabInfo = HiTabTopInfo<Int>(tabCategory.categoryName, defaultColor, selectColor)
            topTabs.add(topTabInfo)
        }

        val topTabLayout = tab_top_layout
        val viewPager = view_pager
        topTabLayout.inflateInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[selectTabIndex])

        topTabLayout.addTabSelectedChangeListener(onTabSelectListener)

        if (viewPager.adapter == null) {
            viewPager.adapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
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
        (viewPager.adapter as HomePagerAdapter).updateTabs(tabCategoryList!!)
    }

    inner class HomePagerAdapter(
        fragmentManager: FragmentManager,
        behavior: Int
    ) : FragmentPagerAdapter(fragmentManager, behavior) {
        val tabs = mutableListOf<TabCategory>()

        val fragments = SparseArray<Fragment>(tabs.size)
        override fun getCount(): Int {
            return tabs.size
        }

        override fun getItem(position: Int): Fragment {
            val tabCategoryId = tabs[position].categoryId.toInt()
            //防止重复加载数据复用问题
            var fragment = fragments.get(tabCategoryId, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(tabCategoryId, fragment)
            }
            return fragment
        }

        //`object`是关键字，需要使用''
        override fun getItemPosition(`object`: Any): Int {
            //如果不加区分，如果导航栏缓存数据和接口数据一致，那么tab对应的viewPage都是不会变的，那么Fragment
            //会执行两次生命周期，所以不能刀切。那么需要判断tab下的生成Fragment的位置是否发生了变化

            //拿到了刷新前，该位置对应的Fragment对象,刷新前和刷新后的对象比较
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)
            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else PagerAdapter.POSITION_NONE
        }

        override fun getItemId(position: Int): Long {
            return tabs[position].categoryId.toLong()

        }

        fun updateTabs(tab: List<TabCategory>) {
            tabs.clear()
            tabs.addAll(tab)
            notifyDataSetChanged()
            //调用notifyDataSetChanged后根部不会起作用，需要复写getItemPosition(),为了能让position刷新，
            //返回PagerAdapter.POSITION_NONE，这样在调用notifyDataSetChanged后viewpage会把所有已加载的Fragment
            //都detach调，然后调用 getItem(position: Int)，再逐个attach上去，再重新执行他们的声明周期。
        }
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