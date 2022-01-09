package com.sum.hi.ui.demo

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/07 01:13
 * @类描述 ${TODO}
 */

fun main() {
//
//    foo()
//    testList()
    testDog()
}

class B {
    lateinit var a: A

    fun test() {
        //::表示创建成员引用或者类引用
        if (::a.isLateinit) {

        }
    }
}

class A {

}

interface study {
    val time: Int//没有方法体,抽象的，需要在构造函数实现
    fun discuss()
    fun learn() {
        println("learn")
    }
}

class student(override val time: Int) : study {
    override fun discuss() {
        TODO("Not yet implemented")
    }
}

open class Address(val name: String) {
    open fun printData() {

    }
}

class Shop() {
    var address: Address? = null
    fun addAddress(address: Address) {
        this.address = address
    }
}

fun test() {
    Shop().addAddress(object : Address("") {
        override fun printData() {
            super.printData()
        }
    })
}

fun foo() {
    val o = object {
        val x = 0
        var y = 0
    }

    o.y = 10
    println("x: ${o.x}, y: ${o.y}")
}

object DataUtils {

    fun <T> isEmpty(list: ArrayList<T>?): Boolean {
        return list?.isEmpty() ?: false
    }

}

fun testList() {
    val list = arrayListOf(1, 2)
    val empty = DataUtils.isEmpty(list)
    println("empty:$empty")
}

/**
 * 类似java的静态方法
 */
class Dog(val name: String) {
    companion object {
        val dog = Dog("haha")
        fun eat() {
            println("吃骨头")
        }
    }
}

fun testDog() {

    println("dag:${Dog.dog}")
    Dog.eat()
}
