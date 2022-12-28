package com.sum.hi.bie_order2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.*
import android.widget.CheckedTextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hilibrary.util.HiResUtil
import com.sum.hi.hiui.cityselector.*
import com.sum.hi.hiui.tab.top.HiTabTopInfo
import kotlinx.android.synthetic.main.dialog_city_selector.*
import java.io.Serializable

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/20 22:11
 * @类描述 ${TODO}
 */
class CitySelectorDialogFragment : AppCompatDialogFragment() {
    private var citySelectListener: onCitySelectListener? = null
    private var topTabSelectIndex: Int = 0
    private var provinceList: List<Province>? = null

    //已经选择的省，包括第一次选择，传递进来的
    //包括省市区三级
    private lateinit var province: Province
    private val defaultColor = HiResUtil.color(R.color.color_333)
    private val selectColor = HiResUtil.color(R.color.color_dd2)

    companion object {
        private const val KEY_PARAMS_DATA_SET = "key_data_set"
        private const val KEY_PARAMS_DATA_SELECT = "key_data_select"
        private const val TAB_PROVINCE = 0
        private const val TAB_CITY = 1
        private const val TAB_DISTRICT = 2
        fun newInstance(
            province: Province?,
            provinceList: List<Province>
        ): CitySelectorDialogFragment {
            val bundle = Bundle()
            bundle.putSerializable(KEY_PARAMS_DATA_SET, province)
            bundle.putSerializable(KEY_PARAMS_DATA_SELECT, provinceList as Serializable)
            val fragment = CitySelectorDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window

        val contentView = layoutInflater.inflate(
            R.layout.dialog_city_selector,
            window?.findViewById(android.R.id.content) ?: container,
            false
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (HiDisplayUtil.getDisplayHeightInPx(inflater.context) * 0.6f).toInt()
        )
        window?.setGravity(Gravity.BOTTOM)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close.setOnClickListener { dismiss() }
        this.province = arguments?.getSerializable(KEY_PARAMS_DATA_SET) as? Province ?: Province()
        this.provinceList = arguments?.getSerializable(KEY_PARAMS_DATA_SELECT) as? List<Province>

        requireNotNull(provinceList) { "params provinceList not be null " }
        refreshTabLayoutCount()
        tab_layout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            //tab选中第二个，viewPager可能还在第一个，则同步viewpager中的选项
            if (view_pager.currentItem != index) {
                view_pager.setCurrentItem(index, false)
            }
        }
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position != topTabSelectIndex) {
                    //去通知tab标签切换
                    tab_layout.defaultSelected(topTabs[position])
                    topTabSelectIndex = position
                }
            }
        })

        view_pager.adapter = CityPagerAdapter { tabIndex, selectDistrict ->
            //tabIndex代表就是哪一个列表，发生了点击事件
            //selectDistrict是点页面选中的数据对象（省市区）
            when (selectDistrict?.type) {
                TYPE_PROVINCE -> {
                    province = selectDistrict as Province
                }
                TYPE_CITY -> {
                    province.selectCity = selectDistrict as City
                }
                TYPE_DISTRICT -> {
                    province.selectDistrict = selectDistrict
                }
            }
            //如果说本次选中的数据对象的type不是区的类型
            if (!TextUtils.equals(selectDistrict?.type, TYPE_DISTRICT)) {
                refreshTabLayoutCount()
            } else {
                citySelectListener?.onCitySelect(province)
                dismiss()
            }

        }
    }

    val topTabs = mutableListOf<HiTabTopInfo<Int>>()

    //根据 province更新tabLayout标签数据
    //province -> 拉起选择器的时候传递过来的
    //province -> 本次拉起选择器的的每一次选择，都会记录到province
    //每一次选择都会调用该方法，更新tablayout
    private fun refreshTabLayoutCount() {
        topTabs.clear()
        //要不要添加一个请选择的tab标签
        var addPleasePickTab = true
        //构建省tab栏
        if (!TextUtils.isEmpty(province.id)) {
            topTabs.add(newTabTopInfo(province.districtName))
        }

        //构建市tab栏
        if (province.selectCity != null) {
            topTabs.add(newTabTopInfo(province.districtName))
        }

        //构建区tab栏
        if (province.selectDistrict != null) {
            topTabs.add(newTabTopInfo(province.districtName))
            addPleasePickTab = false
        }

        if (addPleasePickTab) {
            topTabs.add(newTabTopInfo("请选择"))
        }

        //notifyDataSetChanged它是异步的
        //inflateInfo和defaultSelected是同步的，就会触发addTabSelectedChangeListener，进而触发viewpager.setCurrentItem
        //如果view_pager还没刷新完成，还没从1页变成2页，就会报错
        view_pager.post {
            tab_layout.inflateInfo(topTabs as List<HiTabTopInfo<*>>)
            //addPleasePickTab =true 省市区还没选择完，此时需要选择最后一个tab
            //addPleasePickTab =false,省市区已选择完，就发生第二次进入
            tab_layout.defaultSelected(topTabs[if (addPleasePickTab) topTabs.size - 1 else 0])
        }
    }

    private fun newTabTopInfo(districtName: String?) =
        HiTabTopInfo(districtName, defaultColor, selectColor)

    inner class CityPagerAdapter(private val itemCallBack: (Int, District?) -> Unit) :
        PagerAdapter() {
        //只有三级选择，缓存3个就可以了
        private val views = SparseArray<CityListView>(3)

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            //1.需要完成对应view的创建
            val view = views[position] ?: CityListView(container.context)
            views.put(position, view)//把页面的position和view绑定关联
            //2.给view绑定数据,那么这个数据怎么来position=0（省份页面）
            //3.找到每一个页面position=0(省份)它的选中项
            var selectDistrict: District? = null
            val list = when (position) {
                TAB_PROVINCE -> {
                    selectDistrict = province //如果还没有选择过，是没有高亮的，如果被选中过，那么province是有数据的，就是当前选中项
                    provinceList//dataSets
                }
                TAB_CITY -> {
                    selectDistrict = province.selectCity
                    province.cities
                }
                TAB_DISTRICT -> {
                    selectDistrict = province.selectDistrict
                    province.selectCity!!.districts
                }
                else -> {
                    throw IllegalStateException("pageCount must be less than ${view.size}")
                }
            }
            view.bindData(selectDistrict, list) { select ->
                if (view_pager.currentItem != position) return@bindData
                itemCallBack(position, selectDistrict)
            }
            if (view.parent == null) {
                container.addView(view)
            }
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return topTabs.size
        }

        override fun getItemPosition(`object`: Any): Int {
            //viewpager默认刷新时无效的，只有返回值为POSITION_NONE才会刷新，POSITION_UNCHANGED表示没有改变，不需要刷新
            //但是没有必要一股脑返回POSITION_NONE，因为省份的数据是不会变的，会变的是第二页的城市列表和区列表，
            // 需要根据object，其实就是instantiateItem的返回值，也就是cityListView
            //来判断它是第几个页面
            return if (views.indexOfValue(`object` as CityListView?) > 0) POSITION_NONE else POSITION_UNCHANGED
        }
    }

    inner class CityListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : RecyclerView(context, attrs, defStyleAttr) {
        private lateinit var onItemClick: (District) -> Unit
        private var lastSelectDistrict: District? = null
        private var districtList = ArrayList<District>()
        private var lastSelectIndex = -1
        private var currentSelectIndex = -1
        fun bindData(
            selectDistrict: District?,
            list: List<District>?,
            onItemClick: (District) -> Unit
        ) {
            if (list.isNullOrEmpty()) return
            this.onItemClick = onItemClick
            lastSelectDistrict = selectDistrict
            districtList.clear()
            districtList.addAll(list)
            //canot call this method while recyclerview is computing a layout or scrolling
            //recyclerview 在布局阶段或者滑动阶段 不可以调用notifyDataSetChanged
            //这不是必现的，是在viewpager 刷新的时候，新增了页面，新创建了RecyclerView
            //由于recyclerView可能还没有布局完成，我们紧接着调用了notify
            //使用RecyclerView的时候除了上面的情况外，还会遇到ViewHolder.invailed等经典问题，
            // 遇到这种错误有两种解决办法：1.网上搜索答案解决
            //2. 找到这个异常在源码的位置，根据方法触发的调用链，看到触发时机，
            post {
                adapter?.notifyDataSetChanged()
            }
        }

        init {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    return HiViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.dialog_city_selector_list_item, parent, false)
                    )
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    val nameTv = holder.itemView.findViewById<CheckedTextView>(R.id.tv_name)
                    val district = districtList[position]
                    nameTv.text = district?.districtName
                    nameTv.setOnClickListener {
                        lastSelectDistrict = district
                        currentSelectIndex = position
                        notifyItemChanged(lastSelectIndex)//让本次刚刚点中的样式发生变更
                        notifyItemChanged(position)
                    }
                    //点击之后触发刷新，说明当前item是本次选中项
                    if (currentSelectIndex == position && currentSelectIndex != lastSelectIndex) {
                        onItemClick(district)
                    }

                    //首次进入或者点击之后的刷新item状态的正确性
                    if (lastSelectDistrict?.id == district.id) {
                        currentSelectIndex = position
                        lastSelectIndex = position
                    }

                    //改变item状态
                    nameTv.isChecked = currentSelectIndex == position
                }

                override fun getItemCount(): Int {
                    return provinceList?.size ?: 0
                }

            }
        }
    }

    fun setOnCitySelectListener(listener: onCitySelectListener) {
        this.citySelectListener = listener
    }

    interface onCitySelectListener {
        fun onCitySelect(province: Province)
    }
}