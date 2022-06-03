package com.sum.hi.ui.demo

import java.util.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/16 09:13
 * @类描述 ${TODO}泛型
 */

fun main() {
//    println(Coke().eat().price)
//    Coke().price(Sweet())
//    BlueColor(Blue()).printlnColor()
    test2()
}

interface Drink<T> {
    fun eat(): T

    fun price(t: T)
}

class Sweet {
    val price = 5
}

class Coke : Drink<Sweet> {
    override fun eat(): Sweet {
        println("eat == ")
        return Sweet()
    }

    override fun price(t: Sweet) {
        println("price == ${t.price} ")
    }
}

/*---------泛型参数---------*/

abstract class Color<T>(var t: T/*泛型字段*/) {
    abstract fun printlnColor()
}

class Blue {
    val color = "Bule"
}

class BlueColor(t: Blue) : Color<Blue>(t) {
    override fun printlnColor() {
        println("color == ${t.color}")
    }

    /*---------泛型方法---------*/
//<T>泛型的类型， Class<T>使用它的泛型， 可以将泛型作为一个返回
    fun <T> fromJson(json: String, tClass: Class<T>): T? {
        //获取T的实例
        val t = tClass.newInstance()
        return t
    }
}

/**
 * 泛型约束
 * Java中可以通过有界类型参数来限制参数类型的边界，Kotlin中泛型约束也可以限制参数类型的上界：
 * T extend:后面是T的上界
 * T super:后面是T的下界
 */

//Java
//public static <T extends Comparable<? super T>> void sort(List<T> list){
//
//}

//Kotlin
fun <T : Comparable<T>?> sort(list: List<T>?): Unit {}

fun test2() {
    sort(listOf(1, 2, 3))//Int是 Comparable<Int> 的子类型
    //sort(listOf(Blue(),Blue(),Blue()))//Blue 不是 Comparable<Blue> 的子类型

    val listStr = arrayListOf("A", "B", "C", "D")
    val list = share(listStr, "C")
    println("多个上界 list = $list")
}

//多个上界的情况
//T的父类是CharSequence，并且是Comparable<T> 的子类型
fun <T> share(list: List<T>, thresold: T): List<T>
        where T : CharSequence,
              T : Comparable<T> {
    return list.filter { it > thresold }.map { it }
}

/**
 * 泛型中的 out 与 in
 * 在Kotlin中的out代表协变，in 代表逆变，我们可以将Kotlin的协变看成Java的上界通配符，将逆变看成java的下界通配符：
 */

//Kotlin使用协变
fun sumOfList(list: List<out Number>) {

}

//java 上界通配符
//public void sumOfList(List<? extends Number> list){
//
//}

//Kotlin使用逆变
//fun addNumbers(list: List<in Int>) {
//
//}

//java 下界通配符
//public void addNumbers(List<? super Integer> list){
//
//}

