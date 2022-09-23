package com.sum.hi.ui.search

import com.sum.hi.ui.model.GoodsModel
import java.io.Serializable

/**
 * @author smy
 * @date   2022/9/19 12:14
 * @desc  右键->Generate->kotlin data class form json ->输入className
 */

data class QuickSearchList(val list: List<KeyWord>, val total: Int)

data class KeyWord(val id: String?, val keyWord: String) : Serializable

data class GoodsListSearch(val total: Int, val list: List<GoodsModel>)