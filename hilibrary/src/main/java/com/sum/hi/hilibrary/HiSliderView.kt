package com.sum.hi.hilibrary

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/06/29 21:34
 * @类描述 ${TODO}
 */
class HiSliderView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var menuItemAttrs: MenuItemAttr
    private val MENU_WIDTH = applyUnit(TypedValue.COMPLEX_UNIT_DIP, 100f)
    private val MENU_HEIGHT = applyUnit(TypedValue.COMPLEX_UNIT_DIP, 45f)
    private val MENU_TEXT_SIZE = applyUnit(TypedValue.COMPLEX_UNIT_SP, 14f)

    private val TEXT_COLOR_NORMAL = Color.parseColor("#666666")
    private val TEXT_COLOR_SELECT = Color.parseColor("#DD3127")
    private val BG_COLOR_NORMAL = Color.parseColor("#F7F8F9")
    private val BG_COLOR_SELECT = Color.parseColor("#FFFFFF")

    val menuView = RecyclerView(context)
    val contentView = RecyclerView(context)

    private val MENU_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_menu_item
    private val CONTENT_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_content_item

    init {
        menuItemAttrs = parseMenuItemAttrs(attributeSet)
        orientation = HORIZONTAL
        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER//当列表滑动在最顶部或者最底部的时候，继续拉会出现一个蓝色阴影，这里禁用掉
        menuView.itemAnimator = null //去掉动画

        contentView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null

        addView(menuView)
        addView(contentView)
    }

    fun bindMenuView(
        layoutRes: Int = MENU_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindViewHolder: (HiViewHolder, Int) -> Unit,
        OnItemClick: (HiViewHolder, Int) -> Unit
    ) {
        menuView.layoutManager = LinearLayoutManager(context)
        menuView.adapter = MenuAdapter(layoutRes, itemCount, onBindViewHolder, OnItemClick)
    }

    fun bindContentView(
        layoutRes: Int = CONTENT_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        itemDecoration: RecyclerView.ItemDecoration?,
        layoutManager: RecyclerView.LayoutManager,
        onBindViewHolder: (HiViewHolder, Int) -> Unit,
        OnItemClick: (HiViewHolder, Int) -> Unit
    ) {
        if (contentView.layoutManager == null)
            contentView.layoutManager = layoutManager
        contentView.adapter = ContentAdapter(layoutRes)
        itemDecoration?.let {
            contentView.addItemDecoration(itemDecoration)
        }
        val adapter = contentView.adapter as ContentAdapter
        adapter.update(itemCount, onBindViewHolder, OnItemClick)
        adapter.notifyDataSetChanged()

        contentView.scrollToPosition(0)
    }
    inner class ContentAdapter(val layoutRes: Int) : RecyclerView.Adapter<HiViewHolder>() {
        private lateinit var itemClick: (HiViewHolder, Int) -> Unit
        private lateinit var bindView: (HiViewHolder, Int) -> Unit
        private var count: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            return HiViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            bindView(holder, position)
            holder.itemView.setOnClickListener {
                itemClick(holder, position)
            }
        }

        override fun getItemCount(): Int = count

        fun update(
            itemCount: Int,
            onBindView: (HiViewHolder, Int) -> Unit,
            onItemClick: (HiViewHolder, Int) -> Unit
        ) {
            this.count = itemCount
            this.bindView = onBindView
            this.itemClick = onItemClick
        }

        override fun onViewAttachedToWindow(holder: HiViewHolder) {
            super.onViewAttachedToWindow(holder)
            val remainSpace = (width - paddingLeft - paddingRight - menuView.width)
            val layoutManager = (parent as RecyclerView).layoutManager
            var spanCount = 0
            if (layoutManager is GridLayoutManager){
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager){
                spanCount = layoutManager.spanCount
            }

            if (spanCount > 0){
                val itemWidth = remainSpace / spanCount
                //设置layoutParams的原因，放置icon未加载出来之前，防止上下闪动
                val layoutParams = holder.itemView.layoutParams
                layoutParams.width = itemWidth
                layoutParams.height = itemWidth
                holder.itemView.layoutParams =layoutParams
            }
        }
    }

    inner class MenuAdapter(
        val layoutRes: Int,
        val count: Int,
        val onBindView: (HiViewHolder, Int) -> Unit,
        val OnItemClick: (HiViewHolder, Int) -> Unit
    ) : RecyclerView.Adapter<HiViewHolder>() {
        private var currentSelectIndex = 0

        //上一次选择
        private var lastSelectIndex = 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            val params = RecyclerView.LayoutParams(menuItemAttrs.width, menuItemAttrs.height)
            itemView.layoutParams = params
            itemView.setBackgroundColor(menuItemAttrs.normalBackgroundColor)
            itemView.findViewById<TextView>(R.id.menu_item_title)
                ?.setTextColor(menuItemAttrs.textColor)
            itemView.findViewById<ImageView>(R.id.menu_item_indicator)
                ?.setImageDrawable(menuItemAttrs.indicator)
            return HiViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                currentSelectIndex = position
                notifyItemChanged(position)
                notifyItemChanged(lastSelectIndex)
            }
            if (currentSelectIndex == position) {
                OnItemClick(holder, position)
                lastSelectIndex = currentSelectIndex
            }
            applyItemAttr(position, holder)
            onBindView(holder, position)
        }

        private fun applyItemAttr(position: Int, holder: HiViewHolder) {
            val selected = position == currentSelectIndex
            val titleView: TextView? = holder.findViewById(R.id.menu_item_title)
            val indicatorView: ImageView? = holder.findViewById(R.id.menu_item_indicator)

            indicatorView?.visibility = if (selected) View.VISIBLE else View.GONE
            //size值已经是px单位了
            titleView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                if (selected) menuItemAttrs.selectTextSize.toFloat() else menuItemAttrs.textSize.toFloat()
            )
            holder.itemView.setBackgroundColor(if (selected) menuItemAttrs.selectBackgroundColor else menuItemAttrs.normalBackgroundColor)
            titleView?.isSelected = selected
        }

        override fun getItemCount(): Int = count


    }

    private fun parseMenuItemAttrs(attributeSet: AttributeSet?): MenuItemAttr {
        val typeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.HiSliderView)

        val menuItemWidth =
            typeArray.getDimensionPixelOffset(R.styleable.HiSliderView_menuItemWidth, MENU_WIDTH)
        val menuItemHeight =
            typeArray.getDimensionPixelOffset(R.styleable.HiSliderView_menuItemHeight, MENU_HEIGHT)
        val menuItemTextSize =
            typeArray.getDimensionPixelSize(R.styleable.HiSliderView_menuTextSize, MENU_TEXT_SIZE)
        val menuItemSelectTextSize = typeArray.getDimensionPixelSize(
            R.styleable.HiSliderView_menuItemSelectTextSize,
            MENU_TEXT_SIZE
        )

        val menuItemTextColor =
            typeArray.getColorStateList(R.styleable.HiSliderView_menuItemTextColor)
                ?: generateColorStateList()
        val menuItemIndicator = typeArray.getDrawable(R.styleable.HiSliderView_menuItemIndicator)
            ?: ContextCompat.getDrawable(context, R.drawable.shape_hi_slider_indicator)

        val menuItemBackgroundColor =
            typeArray.getColor(R.styleable.HiSliderView_menuItemBackgroundColor, BG_COLOR_NORMAL)
        val menuItemBackgroundSelectColor = typeArray.getColor(
            R.styleable.HiSliderView_menuItemSelectBackgroundColor,
            BG_COLOR_SELECT
        )
        typeArray.recycle()
        return MenuItemAttr(
            menuItemWidth,
            menuItemHeight,
            menuItemTextColor,
            menuItemBackgroundSelectColor,
            menuItemBackgroundColor,
            menuItemTextSize,
            menuItemSelectTextSize,
            menuItemIndicator
        )
    }

    private fun generateColorStateList(): ColorStateList {
        //一个二维数组, 每个元素都是intArray
        val states = Array(2) { IntArray(2) }
        val colors = IntArray(2)

        colors[0] = TEXT_COLOR_SELECT
        colors[1] = TEXT_COLOR_NORMAL

        //需要指定一个值，状态值
        states[0] = IntArray(1) { android.R.attr.state_selected }
        states[1] = IntArray(1)//第二个元素可以不赋值，那么可以不写

        //控件状态到颜色的映射，需要两个参数，一个状态数组，一个颜色数组
        //就是说，在states[0]状态下使用colors[0]颜色，依次类推
        return ColorStateList(states, colors)
    }

    /**
     * dp转px,sp转px
     */
    fun applyUnit(unit: Int, value: Float): Int {
        return TypedValue.applyDimension(unit, value, resources.displayMetrics).toInt()
    }

    data class MenuItemAttr(
        val width: Int,
        val height: Int,
        val textColor: ColorStateList,
        val selectBackgroundColor: Int,
        val normalBackgroundColor: Int,
        val textSize: Int,
        val selectTextSize: Int,
        val indicator: Drawable?
    )

}