package com.sum.hi.ui.pattern.adapter

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/22 23:25
 * @类描述 ${TODO}
 */
fun main() {
    //类适配器模式
    val adapterClass = AdapterClass()
    adapterClass.apply {
        request1()
        request2()
    }

    //对象适配器模式
    val adaptee = Adaptee()
    val adapterObj = AdapterObj(adaptee)
    adapterObj.apply {
        request1()
        request2()
    }
}