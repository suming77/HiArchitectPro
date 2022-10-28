package com.sum.hi.hi_order

import android.text.TextUtils
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/08 08:45
 * @类描述 ${TODO}
 * internal包名相同的类才能访问
 */
internal object PriceUtil {
     fun subPrice(goodsPrice: String?): String? {
        if (goodsPrice != null) {
            if (goodsPrice.startsWith("￥") && goodsPrice.length > 2) {
                return goodsPrice.substring(1, goodsPrice.length - 1)
            }
            return goodsPrice
        }
        return null
    }

    fun calculate(goodsPrice: String?, amount: Int): String {
        val price = subPrice(goodsPrice)
        if (TextUtils.isEmpty(price)) return ""
        //在做金额加减乘除，不要用基本数据类型，会有精度丢失
        val bigDecimal = BigDecimal(price)
        val multiply = bigDecimal.multiply(BigDecimal(amount))
        //金额数值格式化
        //100*100 = 10,000
        val df = DecimalFormat("###,###.##")
        return df.format(multiply)
    }

}