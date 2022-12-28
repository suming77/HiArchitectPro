package com.sum.hi.ui.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sum.hi.bie_order2.address.Address

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/16 08:09
 * @类描述 ${TODO}
 */
class AddressViewModel : ViewModel() {
     var selectPosition: Int = -1
     var checkAddressItem: AddressItem? = null
    fun queryAddressList(): LiveData<List<Address>> {
        val liveData = MutableLiveData<List<Address>>()
//        ApiFactory.create(AddressApi::class.java).queryAddress(1, 100).enqueue(object :HiCallback<AddressModel>{
//            override fun onSuccess(response: HiResponse<AddressModel>) {
//
//            }
//
//            override fun onFailed(throwable: Throwable) {
//            }
//        })
        val list = mutableListOf<Address>()
        for (i in 1..10) {
            val address = Address("深圳", "宝安", "新安街道", "0", "10086", "广东省", "苏鲜生", "0")
            list.add(address)
        }
        liveData.postValue(list)
        return liveData
    }

    fun saveAddress(
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address> {
        val data = MutableLiveData<Address>()
//        ApiFactory.create(AddressApi::class.java)
//            .addAddress(province, city, area, detail, receiver, phone)
//            .enqueue(object : HiCallback<String> {
//                override fun onSuccess(response: HiResponse<String>) {
//                    if (response.successful) {
//                        val address =
//                            Address(city, area, detail, "", phone, province, receiver, "0")
//                        data.postValue(address)
//                    }
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                }
//
//            })
        val address = Address(city, area, detail, "", phone, province, receiver, "0")
        data.postValue(address)
        return data
    }

    fun updateAddress(
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address> {
        val data = MutableLiveData<Address>()
//        ApiFactory.create(AddressApi::class.java)
//            .updateAddress(province, city, area, detail, receiver, phone)
//            .enqueue(object : HiCallback<String> {
//                override fun onSuccess(response: HiResponse<String>) {
//                    if (response.successful) {
//                        val address =
//                            Address(city, area, detail, "", phone, province, receiver, "0")
//                        data.postValue(address)
//                    }
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                }
//
//            })
        val address = Address(city, area, detail, "", phone, province, receiver, "0")
        data.postValue(address)
        return data
    }

    /**
     * Viewmodle的生命周期是比Activity的生命周期要长的， 在宿主destory的时候重置掉
     */
    override fun onCleared() {
        checkAddressItem = null
        selectPosition = -1
        super.onCleared()
    }
}