package com.sum.hi.ui.pattern.adapter

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/22 23:34
 * @类描述 ${TODO}
 */
class AdapterObj(private val adaptee: Adaptee) : Target {
    override fun request1() {
        adaptee.request1()
    }

    override fun request2() {
        println("AdapterObj:request2")
    }
}