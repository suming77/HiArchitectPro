package com.sum.hi.ui.model

import android.text.TextUtils

/**
 * @author smy
 * @date   2022/7/12 11:07
 * @desc
 */

data class DetailModel(
    val categoryId: String,
    val commentCountTitle: String,
    val commentTags: String,
    val commentModels: List<CommentModel>?,
    val completedNumText: String,
    val createTime: String,
    val flowGoods: List<GoodsModel>?,
    val gallery: List<GoodsModel>?,
    val goodAttr: List<MutableMap<String, String>>?,
    val goodsDescription: String,
    val goodsId: String,
    val goodsName: String,
    val groupPrice: String,
    val hot: Boolean,
    val similarGoods: List<GoodsModel>?,
    val marketPrice: String,
    val shop: Shop,
    val sliderImage: String,
    val sliderImages: List<SliderImage>?,
    val tags: String?
)

data class CommentModel(
    val avatar: String,
    val content: String, val nickName: String
)

data class Shop(
    val completeNum: String,
    val evaluation: String,
    val goodsNum: String,
    val logo: String,
    val name: String
)

fun getPrice(groupPrice: String?, marketPrice: String): String {
    var price: String? = if (TextUtils.isEmpty(groupPrice)) marketPrice else groupPrice
    if (price?.startsWith("¥") != true) {
        price = "¥".plus(price)
    }
    return price

}