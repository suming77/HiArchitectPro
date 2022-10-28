package com.sum.hi.hi_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sum.hi.common.http.ApiFactory
import com.sum.hi.hi_order.address.Address
import com.sum.hi.hi_order.address.AddressApi
import com.sum.hi.hi_order.address.AddressModel
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/11 07:54
 * @类描述 ${TODO}
 */
class OrderViewModel : ViewModel() {
    fun queryMainAddress(): LiveData<Address?> {
        val liveData = MutableLiveData<Address>()
//        ApiFactory.create(AddressApi::class.java).queryAddress(1, 1)
//            .enqueue(object : HiCallback<AddressModel> {
//                override fun onSuccess(response: HiResponse<AddressModel>) {
//                    val list = response?.data?.list
//                    val firstElement = if (list?.isNotEmpty() == true) list[0] else null
//                    liveData.postValue(firstElement)
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    liveData.postValue(null)
//                }
//
//            })
        val address = Address("南山区", "深圳市", "生态科技园", "0", "13800138000", "广东省", "苏先生", "0")
        liveData.postValue(null)
        return liveData
    }
}