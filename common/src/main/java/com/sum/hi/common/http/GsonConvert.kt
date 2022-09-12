package com.sum.hi.common.http

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.hilibrary.restful.HiConvert
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * @Author:   smy
 * @Date:     2022/3/29 22:07
 * @Desc:
 */
class GsonConvert : HiConvert {
    private var gson: Gson

    //初始化json对象
    init {
        gson = Gson()
    }

    /**
     * 拿到data部分的数据去根据dataType去做转换
     */
    override fun <T> convert(rawDate: String, dataType: Type): HiResponse<T> {
        var response: HiResponse<T> = HiResponse()
        try {
            //将data转换为jsonObject
            var jsonObject = JSONObject(rawDate)
            response.code = jsonObject.optInt("code")
            response.msg = jsonObject.optString("msg")

//            var data = jsonObject.optString("data")
            var data = jsonObject.opt("data")//读取为object类型

            //下面data是按照json的格式来解释的，如果data返回的是一个string则会抛出异常
            //JSONObject和JSONArray都是满足解析条件的
            if ((data is JSONObject) or (data is JSONArray)) {
                if (response.code == HiResponse.SUCCESS) {
                    response.data = gson.fromJson(data.toString(), dataType)
                } else {
                    //使用Gson解析一个HashMap的标配写法
                    response.errorData = gson.fromJson<MutableMap<String, String>>(
                        data.toString(),
                        object : TypeToken<MutableMap<String, String>>() {}.type
                    )
                }
            }else{//否则直接赋值给response
                response.data = data as T?
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            response.code = -1
            Log.e("BizInterceptor", "JSONException == "+e.message)
            response.msg = e.message
        }

        //可以rawDate自行解析
        response.rawData = rawDate
        return response
    }
}