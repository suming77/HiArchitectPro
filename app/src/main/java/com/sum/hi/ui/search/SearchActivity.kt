package com.sum.hi.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.sum.hi.common.view.EmptyView
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.hilibrary.util.HiResUtil
import com.sum.hi.hiui.search.HiSearchView
import com.sum.hi.hiui.search.SimpleTextWatcher
import com.sum.hi.ui.R
import kotlinx.android.synthetic.main.activity_search.*

/**
 * @author smy
 * @date   2022/9/19 07:46
 * @desc
 */
@Route(path = "/search/main")
class SearchActivity : AppCompatActivity() {

    private var historyView: HistorySearchView? = null
    private var goodsSearchView: GoodsSearchView? = null
    private var quickSearchView: QuickSearchView? = null
    private var mEmptyView: EmptyView? = null
    var newStatus = -1
    lateinit var viewModel: SearchViewModel

    companion object {
        const val STATUS_EMPTY = 0
        const val STATUS_HISTORY = 1
        const val STATUS_QUICK_SEARCH = 2
        const val STATUS_GOODS_SEARCH = 3
    }

    private val searchListener = View.OnClickListener {
        val keyWord = searchView?.editText?.text?.trim().toString()
        if (keyWord.isNullOrBlank()) {
            return@OnClickListener
        }
        doKeyWordSearch(KeyWord(null, keyWord))

    }

    /**
     * 输入关键词后显示快搜页面
     */
    private val debounceTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            val hasContent = s.isNullOrBlank()
            if (!hasContent) {
                viewModel.quickSearch(s.toString())
                    .observe(this@SearchActivity, Observer { keyWords ->
                        if (keyWords.isNullOrEmpty()) {
                            updateViewStatus(STATUS_EMPTY)
                        } else {
                            updateViewStatus(STATUS_QUICK_SEARCH)
                            quickSearchView?.bindData(keyWords, callback = { keyWord ->
                                Log.e("smy", "debounceTextWatcher == ${keyWord}")
                                doKeyWordSearch(keyWord)
                            })
                        }
                    })
            }
        }
    }

    /**
     * 关键词搜索
     */
    private fun doKeyWordSearch(keyWord: KeyWord) {
        //1.搜索框高亮，搜索词
        searchView?.setKeyWord(keyWord.keyWord, updateHistoryListener)
        //2.keyword存储起来
        viewModel.saveHistory(keyWord)
        //3.发起goodsSearch
        val keyWordClearIconView: View? =
            searchView.findViewById<View>(R.id.id_search_keyword_clear_icon)
        keyWordClearIconView?.isEnabled = false
        viewModel.goodsSearch(keyWord.keyWord, true)
        viewModel.goodsSearchLiveData.observe(this, Observer { goodsList ->
            keyWordClearIconView?.isEnabled = true
            val loadInit = viewModel.pageIndex == 1
            if (goodsList == null) {
                if (loadInit) {
                    updateViewStatus(STATUS_EMPTY)
                }
            } else {
                updateViewStatus(STATUS_GOODS_SEARCH)
                //搜索结果可能需要1s后才能返回，但是你手快点击了keyWord上的一键清除，页面会被回退到空布局或者历史搜索状态
                //那么搜索结果回来后显示搜索结果还是显示空布局？这里统一一下，在搜索结果没有回来之前不允许点击keyWord上的一键清除按钮
                goodsSearchView?.bindData(goodsList, loadInit)
            }
        })
    }

    private val updateHistoryListener = View.OnClickListener {
        if (viewModel.cacheKeyWords.isNullOrEmpty()) {
            updateViewStatus(STATUS_EMPTY)
        } else {
            updateViewStatus(STATUS_HISTORY)
            historyView?.bindData(viewModel.cacheKeyWords)
        }
    }
    private lateinit var searchView: HiSearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        initTopBar()
        updateViewStatus(STATUS_EMPTY)
        queryLocalHistory()
    }

    private fun queryLocalHistory() {
        viewModel.queryLocalHistory().observe(this, Observer { histories ->
            if (histories != null) {
                updateViewStatus(STATUS_HISTORY)
                historyView?.bindData(histories)
            }
        })
    }

    /**
     * 更新页面状态
     */
    private fun updateViewStatus(status: Int) {
        if (status == newStatus) return
        newStatus = status
        var showView: View? = null
        when (status) {
            STATUS_EMPTY -> {
                if (mEmptyView == null) {
                    mEmptyView = EmptyView(this)
                    mEmptyView?.setDesc(HiResUtil.string(R.string.list_empty_desc))

                    mEmptyView?.setIcon(R.string.list_empty)
                }
                showView = mEmptyView
            }

            STATUS_QUICK_SEARCH -> {
                if (quickSearchView == null) {
                    quickSearchView = QuickSearchView(this)
                }
                showView = quickSearchView
            }

            STATUS_GOODS_SEARCH -> {
                if (goodsSearchView == null) {
                    goodsSearchView = GoodsSearchView(this)
                    //分页
                    goodsSearchView?.enableLoadMore({
                        val str = searchView.getKeyWord()
                        if (TextUtils.isEmpty(str)) {
                            return@enableLoadMore
                        }
                        viewModel.goodsSearch(str, false)
                    }, 5)
                }
                showView = goodsSearchView
            }
            STATUS_HISTORY -> {
                if (historyView == null) {
                    historyView = HistorySearchView(context = this)
                    historyView!!.setOnCheckChangeListener { keyWord ->
                        doKeyWordSearch(keyWord)
                    }
                    historyView!!.setOnHistoryClearListener {
                        viewModel.clearHistory()
                        updateViewStatus(STATUS_EMPTY)
                    }
                }
//                HiExecutor.execute(runnable = Runnable {
//                    val cache =
//                        HiCacheManager.getCacheBody<List<KeyWord>>(SearchViewModel.SEARCH_HISTORY_KEY)
//                    historyView.bindData(cache)
//                })


            }
        }

        if (showView != null) {
            //刚刚创建，还没有被添加到ViewGroup上面
            if (showView.parent == null) {
                fl_container.addView(showView)
            }

            //遍历子view显示隐藏
            val childCount = fl_container.childCount
            for (i in 0 until childCount) {
                val childAt = fl_container.getChildAt(i)
                childAt.visibility = if (childAt == showView) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initTopBar() {
        hiNavigationBar.setNavListener(View.OnClickListener {
            onBackPressed()
        })
        val searchButton =
            hiNavigationBar.addRightTextButton(R.string.nav_item_search, R.id.id_nav_item_search)
        searchButton.setTextColor(HiResUtil.getColorStateList(R.color.color_nav_item_search))
        searchButton.isEnabled = false
        searchButton.setOnClickListener(searchListener)

        searchView = HiSearchView(context = this)
        searchView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            HiDisplayUtil.dp2px(38f)
        )
        searchView.setHintText(HiResUtil.string(R.string.search_hint))
        searchView.setClearIconClickListener(updateHistoryListener)
        searchView.setDebounceTextChangeListener(debounceTextWatcher)

        hiNavigationBar.setContainerView(searchView)
    }
}