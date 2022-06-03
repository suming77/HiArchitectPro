package com.sum.hi.ui.demo

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/16 18:15
 * @类描述 ${TODO}扩展函数
 */
fun main() {
    val list = mutableListOf(1, 2, 3, 4)
    list.swap(1, 2)
    println("swap()交换位置后：${list.toString()}")
    Jump.share("伴生对象的扩展函数")
    letFun("haha")
}

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}
//
//fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
//    val temp = this[index1]
//    this[index1] = this[index2]
//    this[index2] = temp
//}

/**
 * 扩展属性
 */

val String.lastChar: Char get() = this[this.length - 1]

class Jump {
    companion object {}
}

fun Jump.Companion.share(str: String) {
    println("str:$str")
}

/**
 * let函数：实际上是一个作用域函数，定义一个变量在一个特定范围，避免一些空指针操作
 *
 * run函数：只接收一个lambda函数作为参数，以闭包的形式返回，返回值作为最后一行，或者指定return的表达式，
 * 在run函数中，可以直接访问实例的共有属性和方法
 *
 * apply函数:与run类似，但是返回值不同，返回的是传入对象本身
 */

fun letFun(str: String) {
    str?.let {
        it.length
    }

    ArrayList<Int>().apply {
        add(1)
        add(1)
        add(1)
        add(1)
    }.let {
        println(it)
    }
}

//使用案例=============================

//为Activity添加find扩展方法，用于通过资源id获取控件

fun <T : View> Activity.find(@IdRes id: Int): T {
    return findViewById(id)
}

//为Int添加onClick扩展方法，用于为资源id对应的控件添加onCLick监听
fun Int.onclick(activity: Activity, click: () -> Unit) {
    activity.find<View>(this).apply {
        setOnClickListener {
            click()
        }
    }
}