package com.sum.hi.ui.demo

import kotlin.math.abs

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/03 11:17
 * @类描述 ${TODO} kotlin 基础
 */

fun main() {
    println("----main----")
//    baseType()
//    arrayType()
//    collectionType()
//    collectionSort()

    val list = arrayListOf(1, 2, 3, 4)
    val result = list.sum { println("item $it") }
    println("result$result")

    val listStr = arrayListOf("1", "2", "3", "4")
    val sum = listStr.toIntSum()(2)
    println("sum:$sum")

    testClosure(10)(20) {
        println("testClosure:$it")
    }

    testJieGou()
    literal()
}

/**
 * 方法字面量,未声明的方法，方法文本，说白了就是一段代码，匿名，可以绑定到一个变量
 */

fun literal() {
    //定义一个变量tem,而改变量的类型就是(Int)->Boolean
    var temp: ((Int) -> Boolean?)? = null
    //{ num -> num > 10 }就是方法字面量
    temp = { num -> num > 10 }
    println("temp:${temp(18)}")
}

/**
 * 匿名类
 */

val fun1 = fun(a: Int, b: Int): Int {
    return a + b
}

/**
 * 结构声明
 */
data class Person(val name: String, val age: Int)

fun testJieGou() {
    val person = Person("sumingyan", 30)
    val (name, age) = person
    println("name:$name, age:$age")
}

/**
 * 闭包与高阶函数
 */
fun testClosure(v1: Int): (v2: Int, (Int) -> Unit) -> Unit {
    return fun(v2, printer: (Int) -> Unit) {
        printer(v1 + v2)
    }
}

/**
 * 高阶函数
 *
 * 函数作为参数
 */
fun List<Int>.sum(callback: (Int) -> Unit): Int {
    var result: Int = 0
    for (i in this) {
        result += i
        callback(i)
    }
    return result
}

/**
 * 函数作为返回值
 */
fun List<String>.toIntSum(): (scale: Int) -> Float {
    println("第一层函数")
    return fun(scale): Float {
        var result: Float = 0f
        for (i in this) {
            result += i.toInt() * scale
        }
        return result
    }
}

fun collectionSort() {
    val num3 = mutableListOf(1, 2, 3, 4, 5)
    //随机排序
    num3.shuffle()
    println("num3: $num3")
    num3.sort()//从小到大
    println("num3: $num3")
    num3.sortDescending()//从大到小
    println("num3: $num3")

    data class Student(val name: String, val age: Int)

    val students = arrayListOf<Student>()
    students.add(Student("java", 90))
    students.add(Student("kotlin", 95))
    students.add(Student("c", 90))
    students.add(Student("c++", 80))
    students.add(Student("php", 40))
    //单条件排序
    students.sortBy { it.age }
    println(students)
    //多条件排序
    students.sortWith(compareBy({ it.age }, { it.name }))
    println(students)

}

/**
 * List是一个有序集合，通过索引访问元素，可重复
 * set是唯一元素集合，无重复对象
 * Map是一组键值对，键是唯一的，值可重复
 */
fun collectionType() {
    //不可变集合
    val stringList = listOf("one", "two", "one")
    println(stringList)
    val setList = setOf("zone", "one", "other")
    println(setList)

    //可变集合
    val mutableListOf = mutableListOf(1, 2, 3, 4, 5)
    mutableListOf.add(5)
    mutableListOf.removeAt(2)
    mutableListOf[0] = 10
    println(mutableListOf)

    val hello = mutableSetOf("H", "e", "l", "l", "o")//自动过滤重复元素
    hello.remove("o")
    println("sets == $hello")

    //集合的加减操作
    val world = setOf("w", "o", "r", "l", "d")
    hello += world
    println(hello)

    /**
     * Map<K,V>不是Collection接口的继承者，但是它也是Kotlin的一种集合类型
     */
    val map = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 4, "key5" to 5)
    println("All keys: ${map.keys}")
    println("All values: ${map.values}")

    if ("key2" in map) println("key2 value:${map["key2"]}")//判断key是否在里面
    if (4 in map.values) println("4 is in the map")//判断value是不是在里面
    if (map.containsKey("key5")) println("key5 is in the map")//判断key是不是在里面

    /**
     * Q1 两个具有相同键值对，但顺序不同的map相等吗？
     * 无论键值对顺序如何，包含相同键值对的map是相等的
     */
    //具有相同键值对的map相等,与顺序无关
    val map2 = mapOf("key2" to 2, "key1" to 1, "key3" to 3, "key4" to 4, "key5" to 5)
    println("map1 == map2 : ${map == map2}")

    map.equals(map2)

    /**
     * Q2 两个具有相同元素，但是顺序不同的 list 相等吗
     */
    val list2 = mutableListOf(2, 1, 3, 4, 5)
    println("list1 == list2 : ${mutableListOf == list2}")

    mutableListOf.equals(list2)
}

fun baseType() {

    var num1 = 10.68//double
    val num2 = 12 //Int
    val num3 = 11f //float

    println("num1:$num1, num2:$num2, num3:$num3")
    num1 = -10.68

    println("num1 abs:${abs(num1)}")
    println("num1 to int: ${num1.toInt()}")

    printType(num1)
    printType(num2)
    printType(num3)
}

fun printType(param: Any) {
    println("$param is ${param::class.simpleName}")
}

fun arrayType() {
    val array = arrayOf(1, 2, 3)
    array[0] = 10
    val arrays = arrayOfNulls<Int>(4)
    arrays[0] = 10
    arrays[1] = 20
    arrays[2] = 30
    arrays[3] = 40
    val arrayList = arrayListOf<Int>()
    arrayList.add(1)
    arrayList.add(2)

    println("array:${array.toString()}")
    println("arrays:${arrays.toString()}")
    println("arrayList:${arrayList.toString()}")
    val array2 = Array(3) { i -> (i * i).toString() }
    println("array2:${array2[2]}")


//    intArrayOf()
//    doubleArrayOf()

    //大小为5，值为[0,0,0,0,0]的数组
    val intArray0 = IntArray(5)
    println("intArray0:$intArray0")
    //大小为5，值为[42,42,42,42,42]的数组
    val intArray4 = IntArray(5) { 42 }
    println("intArray4:$intArray4")
    //使用lambda 初始化数组中的值
    //大小为5，值 [0,1,2,3,4]的整型数组，(值初始化为其索引值)
    val array5 = IntArray(5) { it * 2 }

    println("array5:${array5[4]}")

    //遍历数组的5种方式
    for (item in array5) {
        println(item)
    }

    //带索引遍历数组
    for (i in intArray4.indices) {
        println("i:$i -> ${intArray4[i]}")
    }

    //遍历元素(带索引)
    for ((index, item) in array.withIndex()) {
        println("index$index, item$item")
    }

    //forEach遍历数组, it代表每一项
    array2.forEach { println(it) }

    //forEach增强版， index 和 元素
    arrayList.forEachIndexed { index, item ->
        println("forEachIndexed: $index, $item")
    }

}