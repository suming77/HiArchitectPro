package com.sum.hi.ui.pattern.builder

/**
 * @author smy
 * @date 2022/7/20
 * @desc
 */

fun main() {
    val penjava = PenJava.Builder().color("yellow").width(3f).round(true).build()
    penjava.write()

    val pen = Pen()
    //通过with范围函数实现建造者模式
    with(pen, {
        color = "red"
        width = 6.0f
        round = false
    })
    pen.write()

    //apply拓展函数也可以实现建造者模式，与上面等价
    pen.apply {
        color = "green"
        width = 12f
        round = true
        write()
    }

    //with函数的另一种用法
    //使用引入对象，并适合用lambda表达式的返回结果
    val list = mutableListOf<Int>(1, 2, 3)
    val result = with(list) {
        "The first element is ${first()}, The end element is ${last()}"
    }
    println("result == $result")
}