package com.sum.hi.ui.pattern.decorator

/**
 * @author smy
 * @date 2022/7/19
 * @desc 装饰者模式
 */

fun Panda.bamboo(decorator: () -> Unit) {
    eat()
    println("可以吃竹子")
    decorator()
}

fun Panda.carrot(decorator: () -> Unit) {
    println("可以吃胡萝卜")
    decorator()
}

fun main() {
    Panda().run {
        bamboo {
            carrot { }
        }
    }
}
