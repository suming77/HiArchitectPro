package com.sum.hi.common.city

import com.sum.hi.hilibrary.annotation.HiCall
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hiui.cityselector.City

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/16 15:16
 * @类描述 ${TODO}
 */
internal interface CityApi {
    fun listCities(): HiCall<CityModel>
}