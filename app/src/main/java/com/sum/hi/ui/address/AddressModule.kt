package com.sum.hi.ui.address

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.sum.hi.common.view.EmptyView
import com.sum.hi.hi_order.R
import com.sum.hi.hilibrary.util.HiResUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/27 00:12
 * @类描述 ${TODO}
 */
@Module
@InstallIn(ActivityComponent::class)//生命周期和谁相关
object AddressModule {
    //这里的context并不知道是全局的还是局部的，在声明具有分歧的参数的时候，可以用注解来声明它
    //如果声明为ApplicationContext, 则使用的时候就是用应用的context
    //这里声明为ActivityContext, 则使用的时候就是用activity的context
    @Provides
    @ActivityScoped//在Activity内为单例
    fun emptyView(@ActivityContext context: Context): EmptyView {
        val emptyView = EmptyView(context)
        emptyView.setBackgroundColor(Color.WHITE)
        emptyView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        emptyView.setIcon(R.string.list_empty)
        emptyView.setDesc(HiResUtil.string(R.string.list_empty_desc))
        return emptyView
    }

}