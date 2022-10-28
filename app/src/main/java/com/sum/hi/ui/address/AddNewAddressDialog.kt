package com.sum.hi.ui.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sum.hi.common.city.CityMgr
import com.sum.hi.hi_order.CitySelectorDialogFragment
import com.sum.hi.hi_order.address.Address
import com.sum.hi.hilibrary.util.showToast
import com.sum.hi.hiui.cityselector.Province
import com.sum.hi.ui.R
import kotlinx.android.synthetic.main.dialog_add_new_address.*
import kotlinx.android.synthetic.main.dialog_add_new_address.user_name
import kotlinx.android.synthetic.main.dialog_add_new_address.user_phone

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/14 23:32
 * @类描述 ${TODO}
 */
class AddNewAddressDialog : AppCompatDialogFragment() {
    private var address: Address? = null
    private var saveAddressListener: onSaveAddressListener? = null
    private var selectProvince: Province? = null

    companion object {
        const val KEY_ADDRESS_PARAMS = "key_address"
        fun newInstance(address: Address?): AddNewAddressDialog {
            val bundle = Bundle()
            val dialog = AddNewAddressDialog()
            bundle.putParcelable(KEY_ADDRESS_PARAMS, address)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = arguments?.getParcelable<Address>(KEY_ADDRESS_PARAMS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //如果想要撑满屏幕，则使用Windows的根布局
        //使用android.R.id.content作为父容器，在inflate布局的时候，会使用它的LayoutParams来设置子View的宽高
        val window = dialog?.window
        val root = window?.findViewById<ViewGroup>(android.R.id.content) ?: container
        val contentView = inflater.inflate(R.layout.dialog_add_new_address, root, false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        return contentView
    }

    private val viewModel by viewModels<AddressViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val closeBtn = nav_bar.addRightTextButton(R.string.if_close, R.id.nav_id_close)
//        closeBtn.setOnClickListener { dismiss() }
        user_area.getEditText()
            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_task_more, 0)
        //禁止拉起软键盘，但是可以点击
        //既不会拉起软键盘，还能够相应点击事件，而不是isEnable=false
        user_area.getEditText().isFocusable = false
        user_area.getEditText().isFocusableInTouchMode = false
        user_area.getEditText().setOnClickListener {
            val liveData = CityMgr.getCityData()
            //先清除原来的
            liveData.removeObservers(viewLifecycleOwner)
            liveData.observe(viewLifecycleOwner, Observer {
                //拉起城市选择器
                Log.e("smy", "lsit == ${it?.size}")
                if (it != null) {
                    val dialog = CitySelectorDialogFragment.newInstance(selectProvince, it)
                    dialog.setOnCitySelectListener(object :
                        CitySelectorDialogFragment.onCitySelectListener {
                        override fun onCitySelect(province: Province) {
                            updateAddressPick(province)
                        }
                    })
                    dialog.show(childFragmentManager, "CitySelectorDialogFragment")
                } else {
//                    showToast("城市选择数据为空，无法拉起城市选择器")
                }
            })
        }

        address_detail.getTitleView().gravity = Gravity.TOP
        address_detail.getEditText().gravity = Gravity.TOP
        address_detail.getEditText().isSingleLine = false

        if (address != null) {
            user_name.getEditText().setText(address!!.receiver)
            user_phone.getEditText().setText(address!!.phoneNum)
            user_area.getEditText().setText(address!!.province + address!!.city + address!!.area)
            address_detail.getEditText().setText(address!!.detail)
        }

        action_save_address.setOnClickListener {
            saveAddress()
        }
    }

    private fun updateAddressPick(province: Province) {
        this.selectProvince = province
        user_area.getEditText()
            .setText(province.districtName + province.selectCity + province.selectDistrict?.districtName)
    }

    private fun saveAddress() {
        val name = user_name.getEditText().text.toString().trim()
        val phone = user_phone.getEditText().text.toString().trim()
        val detail = address_detail.getEditText().text.toString().trim()
        val city = user_area.getEditText().text.toString().trim()

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) ||
            TextUtils.isEmpty(detail) || TextUtils.isEmpty(city)
        ) {
            Toast.makeText(context, "数据不能为空", Toast.LENGTH_LONG).show()
            return
        }
        val p = selectProvince?.districtName ?: address?.province
        val c = selectProvince?.selectCity?.districtName ?: address?.city
        val a = selectProvince?.selectDistrict?.districtName ?: address?.area
        if (TextUtils.isEmpty(p)||TextUtils.isEmpty(c)||TextUtils.isEmpty(a)){
            showToast("数据不能为空")
            return
        }
        if (address == null) {
            viewModel.saveAddress(p!!, c!!, a!!, detail, receiver = name, phone = phone)
                .observe(viewLifecycleOwner, observer)
        } else {
            viewModel.updateAddress(
                p!!, c!!, a!!,
                detail,
                receiver = name,
                phone = phone
            )
                .observe(viewLifecycleOwner, observer)
        }
    }

    private val observer = Observer<Address?> {
        saveAddressListener?.onSaveAddress(it)
        dismiss()
    }

    fun setOnSaveAddressListener(listener: onSaveAddressListener) {
        this.saveAddressListener = listener
    }

    interface onSaveAddressListener {
        fun onSaveAddress(address: Address?)
    }
}