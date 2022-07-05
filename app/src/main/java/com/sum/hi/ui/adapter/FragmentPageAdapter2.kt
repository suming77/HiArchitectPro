package com.sum.hi.ui.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle

/**
 * @author tea
 * @date   2022/7/5 11:34
 * @desc
 * 首页 更多功能：使用androidx中的viewpage来实现，为什么不实用viewpage2这个组件呢？
 *
 * viewpage2本身是使用RecyclerView来实现横向和纵向分页，由于首页的tab也是recyclerView，
 * 可能会存在横向和纵向手势滑动冲突的问题
 *
 * andoridx下的viewPage与support包下的有很大的不同，不通的原因在于FragmentPagerAdapter传递的behavior是
 * BEHAVIOR_SET_USER_VISIBLE_HINT时，则与support中的库是一样的，andoridx下的viewPage是向下向上兼容的。
 *
 * 当升级到Androidx之后如果不想改变原来的效果，BEHAVIOR_SET_USER_VISIBLE_HINT在切换Fragment时
 * 还会执行没个Fragment的setUserVisibleHint()方法，有个不好的地方，不管Fragment是否可见都会进入onResume状态
 * 这种已过时。
 *
 * 推荐使用BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT，在加载在创建Fragment的时候，并不是每个Fragment
 * 都会到达onResume状态，只有可见才会进入onResume状态，新创建并且不可见他会停留在onStart状态，在viewpager
 * 左右切换的时候只会执行onPause，就不会走setUserVisibleHint()方法。
 */
class FragmentPageAdapter2(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    /**
     * 1.Fragment切换时，会触发setUserVisibleHint(),和旧版效果一样
     * val adapter =  FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT)
     *
     * 2.推荐：Fragment切换时，会触发onResume，onPause，可以实现数据懒加载，页面懒渲染
     * val adapter =  FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
     */

    override fun getCount(): Int {
            return  0
    }

    override fun getItem(position: Int): Fragment {
        return Fragment()
    }

//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        if (mBehavior == BEHAVIOR_SET_USER_VISIBLE_HINT) {
//            //旧版，触发setUserVisibleHint
//            fragment.setUserVisibleHint(false)
//        }
//
//        mFragments.set(position, fragment)
//        //关键句，添加Fragment进入事务list
//        mCurTransaction.add(container.id, fragment)

//        if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//            //androidx设置初始化执行到onStart()就停止了
//            mCurTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
//        }
//        return fragment
//    }

}