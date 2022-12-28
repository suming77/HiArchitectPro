package com.sum.hi.bie_order2.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/11 07:44
 * @类描述 ${TODO}
 */
data class AddressModel(val total: Int, val list: List<Address>)

@Parcelize
data class Address(
    val area: String,
    val city: String,
    val detail: String,
    val id: String,
    val phoneNum: String,
    val province: String,
    val receiver: String,
    val uid: String
) : Parcelable