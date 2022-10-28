package com.sum.hi.common.city

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sum.hi.common.http.ApiFactory
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.cache.HiCacheManager
import com.sum.hi.hilibrary.executor.HiExecutor
import com.sum.hi.hilibrary.log.HiLog
import com.sum.hi.hiui.cityselector.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/16 15:20
 * @类描述 ${TODO}
 * 获取城市本地数据，优先本地缓存，否则接口请求再更新本地缓存，同一时刻多次调用只有一次生效，
 * 内存常驻，所有考虑收到清理LiveData.value
 */
object CityMgr {
    private const val KEY_CITY_CACHE_SET = "city_cache_set"
    private val liveData = MutableLiveData<List<Province>?>()

    //是否正在加载数据……，同一时刻只有一个线程，或者一处能发起数据的加载
    private val isFetching = AtomicBoolean(false)
    fun getCityData(): MutableLiveData<List<Province>?> {
        //发送过一次数据之后，这个数据List<Province>会存储value
        if (isFetching.compareAndSet(false, true) && liveData.value == null) {
            getCache { cache ->
                if (cache != null) {
                    liveData.postValue(cache)
                    isFetching.compareAndSet(true, false)
                } else {
                    fetchRemote { remote ->
                        liveData.postValue(remote)
                        isFetching.compareAndSet(true, false)
                    }
                }
            }
        }

        return liveData
    }

    private fun fetchRemote(callBack: (List<Province>?) -> Unit) {
//        ApiFactory.create(CityApi::class.java).listCities().enqueue(object : HiCallback<CityModel> {
//            override fun onSuccess(response: HiResponse<CityModel>) {
//                if (response.data?.list.isNullOrEmpty() == false) {
//                    groupProvince(response.data?.list!!) { provinceList ->
//                        saveGroupProvince(provinceList)
//                        callBack(provinceList)
//                    }
//                } else {
//                    HiLog.e("response data list is empty or null")
//                    callBack(null)
//                }
//            }
//
//            override fun onFailed(throwable: Throwable) {
//                if (!throwable.message.isNullOrEmpty()) HiLog.e(throwable.message)
//                callBack(null)
//            }
//
//        })

        //省
        val list = mutableListOf<District>()
        val d = District()
        d.id = "1"
        d.pid = "0"
        d.districtName = "中国"
        d.type = "0"
        val d2 = District()
        d2.id = "2"
        d2.pid = "1"
        d2.districtName = "北京"
        d2.type = "1"
        val d3 = District()
        d3.id = "3"
        d3.pid = "1"
        d3.districtName = "安徽"
        d3.type = "1"
        val d4 = District()
        d4.id = "4"
        d4.pid = "1"
        d4.districtName = "福建"
        d4.type = "1"
        val d5 = District()
        d5.id = "5"
        d5.pid = "1"
        d5.districtName = "甘肃"
        d5.type = "1"
        val d6 = District()
        d6.id = "6"
        d6.pid = "1"
        d6.districtName = "广东"
        d6.type = "1"
        val d7 = District()
        d7.id = "7"
        d7.pid = "1"
        d7.districtName = "河南"
        d7.type = "1"

        //市
        val dd = District()
        dd.id = "46"
        dd.pid = "3"
        dd.districtName = "马鞍山"
        dd.type = "2"
        val dd2 = District()
        dd2.id = "47"
        dd2.pid = "1"
        dd2.districtName = "宿州"
        dd2.type = "2"
        val dd3 = District()
        dd3.id = "48"
        dd3.pid = "4"
        dd3.districtName = "铜陵"
        dd3.type = "2"
        val dd4 = District()
        dd4.id = "49"
        dd4.pid = "4"
        dd4.districtName = "惠南"
        dd4.type = "2"
        val dd5 = District()
        dd5.id = "50"
        dd5.pid = "5"
        dd5.districtName = "黄山"
        dd5.type = "2"
        val dd6 = District()
        dd6.id = "51"
        dd6.pid = "5"
        dd6.districtName = "刘安"
        dd6.type = "2"
        val dd7 = District()
        dd7.id = "52"
        dd7.pid = "6"
        dd7.districtName = "信宜"
        dd7.type = "2"

        //区
        val ddd = District()
        ddd.id = "53"
        ddd.pid = "52"
        ddd.districtName = "马鞍山"
        ddd.type = "3"
        val ddd2 = District()
        ddd2.id = "54"
        ddd2.pid = "52"
        ddd2.districtName = "宿州"
        ddd2.type = "3"
        val ddd3 = District()
        ddd3.id = "51"
        ddd3.pid = "4"
        ddd3.districtName = "铜陵"
        ddd3.type = "3"
        val ddd4 = District()
        ddd4.id = "51"
        ddd4.pid = "4"
        ddd4.districtName = "惠南"
        ddd4.type = "3"
        val ddd5 = District()
        ddd5.id = "50"
        ddd5.pid = "5"
        ddd5.districtName = "黄山"
        ddd5.type = "3"
        val ddd6 = District()
        ddd6.id = "50"
        ddd6.pid = "5"
        ddd6.districtName = "刘安"
        ddd6.type = "3"
        val ddd7 = District()
        ddd7.id = "49"
        ddd7.pid = "6"
        ddd7.districtName = "信宜"
        ddd7.type = "3"
        list.add(d)
        list.add(d2)
        list.add(d3)
        list.add(d4)
        list.add(d5)
        list.add(d6)
        list.add(d7)

        list.add(dd)
        list.add(dd2)
        list.add(dd3)
        list.add(dd4)
        list.add(dd5)
        list.add(dd6)
        list.add(dd7)

        list.add(ddd)
        list.add(ddd2)
        list.add(ddd3)
        list.add(ddd4)
        list.add(ddd5)
        list.add(ddd6)
        list.add(ddd7)
        groupProvince(list) { provinceList ->
            Log.e("smy", "provinceList == ${provinceList?.size}")
            saveGroupProvince(provinceList)
            callBack(provinceList)
        }

    }

    //持久化本地
    private fun saveGroupProvince(groupList: List<Province>?) {
        if (groupList.isNullOrEmpty()) return
        HiExecutor.execute(runnable = Runnable {
            HiCacheManager.saveCacheInfo(KEY_CITY_CACHE_SET, groupList)
        })
    }

    //对数据进行分组，生成数据结构
    private fun groupProvince(list: List<District>, callBack: (List<Province>?) -> Unit) {
        //是为了收集所有的省，同时也是为了Type_CITY,快速找到自己所在Province对象
        val provinceMap = hashMapOf<String, Province>()
        //是为了TYPE_DISTRICT 快速找到自己所在的对象
        val cityMap = hashMapOf<String, City>()
        Log.e("smy", "lists0 = ${list.size}")
        HiExecutor.execute(runnable = Runnable {
            for (element in list) {
                Log.e("smy", "element.type = ${element.type}")
                if (element.id.isNullOrEmpty()) continue
                when (element.type) {
                    TYPE_COUNTRY -> {
                        //ignore
                    }
                    TYPE_PROVINCE -> {
                        val province = Province()
                        //类型不匹配，无法强转，拷贝
                        District.copyDistrict(element, province)
                        Log.e("smy", "province = $province")
                        provinceMap[element.id!!] = province
                    }
                    TYPE_CITY -> {
                        Log.e("smy", "TYPE_CITY = ${element.pid}")
                        val city = City()
                        val province = provinceMap[element.pid]
                        province?.cities?.add(city)
                        cityMap[element.id!!] = city
                    }
                    TYPE_DISTRICT -> {
                        Log.e("smy", "TYPE_DISTRICT = ${element.pid}")
                        val city = cityMap[element.pid!!]
                        city?.districts?.add(element)
                    }
                }
            }
            Log.e("smy", "provinceMap.values = ${provinceMap.values}")
            callBack(ArrayList(provinceMap.values))
        })
    }

    private fun getCache(callBack: (List<Province>?) -> Unit) {
        HiExecutor.execute(runnable = Runnable {
            val cache = HiCacheManager.getCacheBody<List<Province>?>(KEY_CITY_CACHE_SET)
            callBack.invoke(cache)
        })
    }
}