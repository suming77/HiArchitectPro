package com.sum.hi.ui.pattern.builder

/**
 * @author smy
 * @date 2022/7/20
 * @desc
 */
class Pen {
    var color: String = "white"
    var width: Float = 1.0f
    var round = false
    fun write() {
        println("kotlin:color == $color | width == $width | round == $round")
    }
}