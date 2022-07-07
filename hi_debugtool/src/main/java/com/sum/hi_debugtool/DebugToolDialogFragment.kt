package com.sum.hi_debugtool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.sum.hi.hilibrary.util.HiDisplayUtil

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/08 00:00
 * @类描述 ${TODO}
 */
class DebugToolDialogFragment : AppCompatDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //如果使用container来当作parent这种方法可能会发现无法调整宽高问题
//        val view = inflater.inflate(R.layout.dialog_debug_tool, container, false)

        //解决方案：获取dialog的根布局，如果使用android.R.id.content这个ViewGroup来解析这个布局
        //那么在解析布局的的时候就会根据父容器的layoutparams来设定view的layoutParams,就可以设定宽高了
        val parent = dialog?.window?.findViewById<ViewGroup>(android.R.id.content) ?: container
        val view = inflater.inflate(R.layout.dialog_debug_tool, parent, false)

        dialog?.window?.setLayout(
            (HiDisplayUtil.getDisplayWidthInPx(view.context) * 0.8f).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //如果把这个设置到布局当中你会发现没有效果，这里直接设置到窗口上面
        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_white_raduis8)
        return view

    }
}