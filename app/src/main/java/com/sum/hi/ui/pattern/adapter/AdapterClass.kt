package com.sum.hi.ui.pattern.adapter

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/22 23:22
 * @类描述 ${TODO}
 */
class AdapterClass : Adaptee(), Target {
    /**
     * 由于Adaptee没有实现request2()，所以这里需要实现request2()
     */
    override fun request2() {
        println("AdapterClass:request2")
    }
}