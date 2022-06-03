package com.sum.hi.ui.demo

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/16 16:55
 * @类描述 ${TODO}kotlin注解
 */
fun main() {
    fire(ApiGetArticles())
}

@Target(AnnotationTarget.CLASS)
annotation class ApiDoc(val name: String)

@ApiDoc("修饰类")
class Box {
    //    @ApiDoc("修饰变量")
    val num = 6

    //    @ApiDoc("修饰方法")
    fun share() {

    }
}

public enum class Method {
    GET,
    POST
}

@Target(AnnotationTarget.CLASS)//类中可以用
@Retention(AnnotationRetention.RUNTIME)//运行时可见
annotation class HttpMethod(val method: Method)

interface Api {
    val name: String
    val version: String
        get() = "1.0.0"
}

@HttpMethod(Method.POST)
class ApiGetArticles : Api {
    override val name: String
        get() = "api/articles"
}

fun fire(api: Api) {
    val annotations = api.javaClass.annotations
    val httpMethod = annotations.find { it is HttpMethod } as? HttpMethod
    println("通过该接口调用函数需要 ${httpMethod?.method} 请求方法")
}