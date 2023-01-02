package com.sum.hi.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.sum.hi.common.component.HiBaseFragment
import com.sum.hi.common.view.EmptyView
import com.sum.hi.common.view.loadUrl
import com.sum.hi.hilibrary.CategoryItemDecoration
import com.sum.hi.hiui.tab.bottom.HiTabBottomLayout
import com.sum.hi.ui.R
import com.sum.hi.ui.home.GoodsListActivity
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.layout_content_loading_view.view.*
import com.sum.hi.ui.model.Subcategory
import com.sum.hi.ui.model.TabCategory

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
class CategoryFragment : HiBaseFragment() {
    private lateinit var tabCategoryList: MutableList<TabCategory>
    private var emptyView: EmptyView? = null
    private val SPAN_COUNT = 3

    override fun getLayoutId(): Int {
        return R.layout.fragment_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(root_container)
        queryCategoryList()
    }

    var goodsCount = 5
    private fun queryCategoryList() {
/*        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
//                    onQueryCategoryList(response.data!!)
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }

            })*/


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

        onQueryCategoryList(tabCategoryList)
        slider_view.postDelayed({ content_loading.content_loading.hide() }, 2000)
    }

    fun getGoodsCount(): String {
        goodsCount++
        return goodsCount.toString()
    }

    private fun onQueryCategoryList(data: List<TabCategory>) {
        if (!isAlive) return
        emptyView?.visibility = View.GONE
        slider_view.visibility = View.VISIBLE

        slider_view.bindMenuView(itemCount = data.size,
            onBindViewHolder = { holder, position ->
//                holder.menu_item_title kotlin不支持跨模块调用，无法直接访问
//            holder.itemView.menu_item_title  这种会每次都findViewById
                val category = data[position]
                holder.findViewById<TextView>(R.id.menu_item_title)?.text = category.categoryName

            }, OnItemClick = { holder, position ->
                val category = data[position]
                querySubcategoryList(category.categoryId, position)
            })
    }

    private fun querySubcategoryList(categoryId: String, position: Int) {
/*
        ApiFactory.create(CategoryApi::class.java).querySubcategoryList(categoryId)
            .enqueue(object : HiCallback<List<Subcategory>> {
                override fun onSuccess(response: HiResponse<List<Subcategory>>) {
                    if (response.code == 200 && response.data != null) {
//                        querySubcategoryListSuccess(response.data!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                }

            })
*/

        val tabCategory = tabCategoryList[position]
        val subCategoryList = mutableListOf<Subcategory>()
        Log.e("smy", "tabCategory$tabCategory")
        for (i in 0..tabCategory.goodsCount.toInt()) {
            if (i < position + 2) {
                subCategoryList.add(
                    Subcategory(
                        tabCategory.categoryId,
                        tabCategory.categoryName,
                        "0",
                        "",
                        i.toString(),
                        tabCategory.categoryName + i
                    )
                )
            } else {
                subCategoryList.add(
                    Subcategory(
                        tabCategory.categoryId,
                        tabCategory.categoryName + "haha",
                        "1",
                        "",
                        i.toString(),
                        tabCategory.categoryName + i
                    )
                )
            }

        }
        querySubcategoryListSuccess(subCategoryList)
    }

    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)
    private val groupSpanSizeOffset = SparseIntArray()
    private val spanSIzeLookUp = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName = subcategoryList[position].groupName
            val nextGroupName =
                if (position + 1 < subcategoryList.size) subcategoryList[position + 1].groupName else null

            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                //当前位置和下一个位置不在同一个分组
                //1.要拿到当前组position（所在组）在groupSpanSizeOffset 的索引下标
                //2.拿到当前组前一组存储的spanSizeOffset偏移量
                //3.给当前组最后一个item分配spansize count

                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                val lastGroupOffset = if (size <= 0) 0 else if (indexOfKey >= 0) {
                    //说明当前组的偏移量记录，已经存在了 groupSpanSizeOffset , 这种情况上下滑动
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
                } else {
                    //说明当前组的偏移量记录，还没有存在于groupSpanSizeOffset，这种情况发生在第一次布局时
                    //得到前面所有组的偏移量之和
                    groupSpanSizeOffset.valueAt(size - 1)
                }
                //           3            ( 6          5              %    3) 第几列 = 0 则处于第一列
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT

                if (indexOfKey < 0) {
                    //得到当前组和前面所有组的spansize 偏移量之和
                    val groupOffset = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }
            return spanSize
        }
    }

    private val subcategoryList = mutableListOf<Subcategory>()
    private val decoration = CategoryItemDecoration({ position ->
        subcategoryList[position].groupName
    }, SPAN_COUNT)

    private fun querySubcategoryListSuccess(data: List<Subcategory>) {
        decoration.clear()
        groupSpanSizeOffset.clear()
        subcategoryList.clear()
        subcategoryList.addAll(data)

        if (layoutManager.spanSizeLookup != spanSIzeLookUp) {
            layoutManager.spanSizeLookup = spanSIzeLookUp
        }
        slider_view.bindContentView(itemCount = data.size,
            layoutManager = layoutManager,
            itemDecoration = decoration,
            onBindViewHolder = { holder, position ->
                val subCategory = data[position]
                holder.findViewById<ImageView>(R.id.content_item_image)
                    ?.loadUrl(subCategory.subcategoryIcon)
                holder.findViewById<TextView>(R.id.content_item_title)?.text =
                    subCategory.subcategoryName
            }, OnItemClick = { holder, position ->
                val subcategory = data[position]
/*                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRouter.startActivity(requireContext(), bundle, HiRouter.Destination.GOODS_LIST)*/
                context?.startActivity(Intent(requireActivity(), GoodsListActivity::class.java))
                showToast("you touch me :$position")
            })
    }

    private fun showEmptyView() {
        if (!isAlive) return
        if (emptyView == null) {
            emptyView = EmptyView(requireContext())
            emptyView?.setIcon(R.string.if_empty3)
            emptyView?.setDesc(getString(R.string.list_empty_desc))
            emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                queryCategoryList()
            })
            emptyView?.setBackgroundColor(Color.WHITE)
            emptyView?.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            root_container.addView(emptyView)
        }


        slider_view.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        slider_view.contentView.adapter?.notifyDataSetChanged()
    }
}