package com.sum.hi.ui.address

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.sum.hi.bie_order2.address.Address
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.hilibrary.util.HiResUtil
import com.sum.hi.hiui.search.IconFontTextView
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/27 22:25
 * @类描述 ${TODO}
 */
class AddressItem(
    var address: Address?,
    val supportFragmentManager: FragmentManager,
    val removeCallBack: (Address, AddressItem) -> Unit,
    val itemClickCallback: (Address?) -> Unit,
    val viewModel: AddressViewModel
) :
    HiDataItem<Address, HiViewHolder>() {

    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        val user_name = holder.itemView.findViewById<TextView>(R.id.user_name)
        val user_phone = holder.itemView.findViewById<TextView>(R.id.user_phone)
        val user_address = holder.itemView.findViewById<TextView>(R.id.user_address)
        val delete = holder.itemView.findViewById<IconFontTextView>(R.id.delete)
        val default_address = holder.itemView.findViewById<IconFontTextView>(R.id.default_address)
        val edit = holder.itemView.findViewById<IconFontTextView>(R.id.edit)

        user_name.text = address?.receiver
        user_phone.text = address?.phoneNum
        user_address.text = address?.province + address?.city + address?.area + address?.detail
        edit.setOnClickListener {
            val dialog = AddNewAddressDialog.newInstance(address)
            dialog.setOnSaveAddressListener(object : AddNewAddressDialog.onSaveAddressListener {
                override fun onSaveAddress(newAddress: Address?) {
                    address = newAddress
                    refreshItem()
                }
            })
            dialog.show(supportFragmentManager, "AddNewAddressDialog")
        }
        delete.setOnClickListener {
            AlertDialog.Builder(context).setMessage("确定要删除吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { dialog, which ->
                    dialog.dismiss()

                }
        }
        holder.itemView.setOnClickListener {
            itemClickCallback(address)
        }
        default_address.setOnClickListener {
            viewModel.selectPosition = position
            viewModel.checkAddressItem?.refreshItem()
            viewModel.checkAddressItem = this
            refreshItem()
        }

        val select = viewModel.selectPosition == position && viewModel.checkAddressItem == this
        default_address.setTextColor(HiResUtil.color(if (select) R.color.color_dd2 else R.color.color_999))
        default_address.setText(if (select) "默认地址" else "设置默认地址")

        default_address.setCompoundDrawablesRelativeWithIntrinsicBounds(
            if (select) R.drawable.rotate_daisy else 0,
            0,
            0, 0
        )

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.activity_address_list_item
    }


}