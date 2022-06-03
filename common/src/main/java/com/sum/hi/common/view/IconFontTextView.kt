package com.sum.hi.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.jar.Attributes

/**
 * @创建者 mingyan.su
 * @创建时间 2022/05/08 09:53
 * @类描述 ${TODO}
 * iconfont比图片小太多，它还是尺量的，可以无限缩放，而且同时可以在布局资源文件当中绑定inc码，而不必在代码中设置
 * 
 * 用以支持全局iconfont资源引用，可以在布局文件中直接设置text
 *
 * JvmOverloads为了解决java不能重载Kotlin有默认参数的方法
 */
class IconFontTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {
    /**
     * init初始化块会与属性初始化块交叉执行
     */
    init {
        //加载assets目录下的资源 Typeface用于指定字体和字体样式等,createFromAsset从指定的资源路径指定字体
        val typeface = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
        setTypeface(typeface)
    }
}