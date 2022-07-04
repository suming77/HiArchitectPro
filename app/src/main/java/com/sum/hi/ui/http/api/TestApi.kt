package com.sum.hi.ui.http.api

import com.google.gson.JsonObject
import com.sum.hi.hilibrary.annotation.Filed
import com.sum.hi.hilibrary.annotation.GET
import com.sum.hi.hilibrary.annotation.HiCall
import org.json.JSONObject

/**
 * @Author:   smy
 * @Date:     2022/3/30 0:56
 * @Desc:
 */
interface TestApi {
    @GET("cities")
    fun listCity(@Filed("name") name: String): HiCall<JsonObject>
}