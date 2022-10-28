package com.sum.hi.ui.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sum.hi.common.view.EmptyView
import com.sum.hi.hi_order.address.Address
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_address_list.*
import javax.inject.Inject

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/27 00:04
 * @类描述 ${TODO}
 */
//通过hilt完成对象的依赖注入
@AndroidEntryPoint
@Route(path = "/address/list"/*, extras = RouterFlag.FLAG_LOGIN*/)
class AddressListActivity : AppCompatActivity() {
    val viewModel by viewModels<AddressViewModel>()

    @Inject
    lateinit var emptyView: EmptyView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        initView()
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        nav_bar.addRightTextButton("添加地址", R.id.nav_id_add_address).setOnClickListener {
            showAddressEditDialog()
        }
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = HiAdapter(this)
        //因为开始显示的是空布局，这里注册一个adapter数据的变化，因为需要反注册所以提取为成员变量
        recycler_view.adapter?.registerAdapterDataObserver(adapterObservable)

        viewModel.queryAddressList().observe(this, Observer {
            if (it.isNullOrEmpty().not()) {
                bindData(it)
            } else {
                showEmptyView(true)
            }
        })
    }

    private fun bindData(it: List<Address>?) {
        val items = arrayListOf<AddressItem>()
        it?.forEach { address ->
            items.add(newAddressItem(address))
        }
        val adapter = recycler_view.adapter as HiAdapter
        adapter.clearItems()
        adapter.addItems(items, true)
    }

    private fun newAddressItem(address: Address?): AddressItem {
        return AddressItem(address, supportFragmentManager, itemClickCallback = { address ->
            val intent = Intent()
            intent.putExtra("result", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, removeCallBack = { address, addressItem ->
//            viewModel.delete()
//            val adapter= recycler_view.adapter as HiAdapter
//            adapter.removeItem(addressItem)
            //会触发onItemRangeRemoved方法
            addressItem.removeItem()
        }, viewModel = viewModel)

    }

    private fun showAddressEditDialog() {
        val addNewAddressDialog = AddNewAddressDialog.newInstance(null)
        addNewAddressDialog.setOnSaveAddressListener(object :
            AddNewAddressDialog.onSaveAddressListener {
            override fun onSaveAddress(address: Address?) {
                val adapter: HiAdapter? = recycler_view.adapter as HiAdapter?
                adapter?.addItem(0, newAddressItem(address), true)
            }
        })
        addNewAddressDialog.show(supportFragmentManager, "AddNewAddressDialog")
    }

    private val adapterObservable = object : RecyclerView.AdapterDataObserver() {
        //数据插入
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showEmptyView(false)
        }

        //数据移除
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            recycler_view.post {
                if (recycler_view.adapter!!.itemCount <= 0) {
                    showEmptyView(true)
                }
            }
        }
    }

    private fun showEmptyView(showEmptyView: Boolean) {
        recycler_view.isVisible = !showEmptyView
        emptyView.isVisible = showEmptyView
        if (emptyView.parent == null && showEmptyView) {
            roo_layout.addView(emptyView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recycler_view?.adapter?.unregisterAdapterDataObserver(adapterObservable)
    }
}