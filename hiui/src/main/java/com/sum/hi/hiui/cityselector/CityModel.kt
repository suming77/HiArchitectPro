package com.sum.hi.hiui.cityselector

import android.os.Parcelable
import java.io.Serializable

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/16 10:57
 * @类描述 ${TODO}
 */
const val TYPE_COUNTRY = "0"
const val TYPE_PROVINCE = "1"
const val TYPE_CITY = "2"
const val TYPE_DISTRICT = "3"

open class Province : District(), Serializable {
    //该省份下面所有的市
    var cities = ArrayList<City>()

    //选择的市
    var selectCity: City? = null

    //选择的区
    var selectDistrict: District? = null
}

open class City : District(), Serializable {
    var districts = ArrayList<District>()
}

/**
 * 城市类型：type 0:国，1：省，2：市，3：区
 * districtName 地区名
 * pid 父级id
 */
open class District : Serializable {
    var districtName: String? = null
    var id: String? = null
    var pid: String? = null
    var type: String? = null

    companion object {
        fun copyDistrict(src: District, dest: District) {
            dest.id = src.id
            dest.type = src.type
            dest.pid = src.pid
            dest.districtName = src.districtName
        }
    }
}