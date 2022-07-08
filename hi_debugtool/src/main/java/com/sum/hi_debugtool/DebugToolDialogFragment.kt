package com.sum.hi_debugtool

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.hilibrary.util.HiDisplayUtil
import kotlinx.android.synthetic.main.dialog_debug_tool.*
import org.w3c.dom.Text
import java.lang.reflect.Method

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/08 00:00
 * @类描述 ${TODO}
 */
class DebugToolDialogFragment : AppCompatDialogFragment() {
    private val debugTools = arrayOf(DebugTool::class.java)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                view.context,
                R.drawable.shape_divider
            )!!
        )

        val functions = mutableListOf<DebugFunction>()
        val size = debugTools.size
        for (i in 0 until size) {
            val claz = debugTools[i]
            //通过空惨构造函数，newInstance获取实例对象
            val target = claz.getConstructor().newInstance()
            val declaredMethods = target.javaClass.declaredMethods
            //扫码里面所有的方法,这个类里面的所有方法都要去扫描一遍
            for (method in declaredMethods) {
                var name = ""
                var desc = ""
                var enable = false
                val annotation = method.getAnnotation(HiDebug::class.java)
                if (annotation != null) {//已标记注解
                    name = annotation.name
                    desc = annotation.desc
                    enable = true
                } else {
                    //这个方法有返回值
                    method.isAccessible = true
                    name = method.invoke(target) as String
                }
                val function = DebugFunction(name, desc, method, enable, target)
                functions.add(function)
            }
        }

        recycler_view.addItemDecoration(dividerItemDecoration)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter =DebugToolsAdapter(functions)

    }

    inner class DebugToolsAdapter(val list: List<DebugFunction>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = layoutInflater.inflate(R.layout.layout_hi_debug_tools_item, parent, false)
            return object : RecyclerView.ViewHolder(view) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val debugFunction = list[position]
            val itemTitle = holder.itemView.findViewById<TextView>(R.id.item_title)
            val item_desc = holder.itemView.findViewById<TextView>(R.id.item_desc)

            itemTitle.text = debugFunction.name
            if (TextUtils.isEmpty(debugFunction.desc)) {
                item_desc.visibility = View.GONE
            } else {
                item_desc.visibility = View.VISIBLE
                item_desc.text = debugFunction.desc
            }

            if (debugFunction.enable) {
                holder.itemView.setOnClickListener {
                    debugFunction.invoke()
                }
            }
        }

        override fun getItemCount(): Int = list.size

    }

    data class DebugFunction(
        val name: String,
        val desc: String,
        val method: Method,
        val enable: Boolean,
        val target: Any
    ) {
        fun invoke() {
            //调用method方法
            method.invoke(target)
        }
    }
}