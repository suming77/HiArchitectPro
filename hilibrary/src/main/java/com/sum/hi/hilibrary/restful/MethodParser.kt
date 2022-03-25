package com.sum.hi.hilibrary.restful

import com.sum.hi.hilibrary.annotation.*
import java.lang.IllegalStateException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Author:   smy
 * @Date:     2022/3/23 22:09
 * @Desc:
 */
class MethodParser(val baseUrl: String, method: Method, args: Array<Any>) {
    private lateinit var doMainUrl: String
    private var formPost: Boolean = true
    private var httpMethod: Int = 0
    private lateinit var relativeUrl: String
    private var returnType: Type? = null
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, Any> = mutableMapOf()


    init {
        //parse method annotations such get headers post baseurl
        parseMethodAnnotations(method)

        //parse method parameters such as path filed
        parseMethodParameters(method, args)

        //parse method genric return type
        parseMethodReturnType(method)
    }

    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class) {
            throw IllegalStateException(
                String.format(
                    "method %s must be type of HiCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        //如果这个泛型返回类型，有泛型参数
        if (genericReturnType is ParameterizedType) {
            //泛型参数的个数
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) {//只有一个泛型参数
                "method %s can only has one generic return type"
            }
            returnType = actualTypeArguments[0]
        } else {//抛出警告
            throw IllegalStateException(
                String.format(
                    "method %s must has one gerneric return type",
                    method.name
                )
            )
        }
    }

    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        val parameterAnnotations = method.parameterAnnotations
        val equals = parameterAnnotations.size == args.size
        require(equals) {//如果条件不满足才会执行后面的异常
            String.format(
                "arguments annotations count %s dont match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        //args
        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) { "filed can only has one annotation :index = $index" }
            val value = args[index]
            require(isPrimitive(value)) { "8 basic types are supported for now , index = $index" }
            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                val value = args[index]
                parameters[key] = value
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    val newRelativeUrl = relativeUrl.replace("${replaceName}", replacement)
                    relativeUrl = newRelativeUrl
                }
            } else {
                throw IllegalStateException("cannot handler parameter annotation :" + annotation.javaClass.toString())
            }

        }


    }

    private fun isPrimitive(value: Any): Boolean {
        //String
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            //int byte short long boolean char double float
            //通过反射获取到包装类型中的type字段，Integer,Boolean中都有一个type字段
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true//是基本数据类型
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return false
    }

    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.POST
                formPost = annotation.fromPost
            } else if (annotation is Headers) {
                val headerArray = annotation.value
                for (header in headerArray) {
                    val colon = header.indexOf(":")
                    check(!(colon == 0) || colon == -1) {
                        String.format(
                            "@headers value must be in the form [name:value], but found [%s]",
                            header
                        )
                    }
                    val name = header.substring(0, colon)
                    val value = header.substring(colon + 1).trim()
                    headers[name] = value
                }
            } else if (annotation is BaseUrl) {
                doMainUrl = annotation.value
            } else {
                throw IllegalStateException("can not handle method annotation:" + annotation.javaClass.toString())
            }

            //校验
            require(!(httpMethod != HiRequest.METHOD.GET) && !(httpMethod != HiRequest.METHOD.POST)) {
                String.format("method %s must has one of GET ，POST", method.name)
            }
        }

        if (doMainUrl == null) {
            doMainUrl = baseUrl
        }
    }

    fun newRequest():HiRequest {
        var request= HiRequest()
        request.doMainUrl = doMainUrl
        request.returnType = returnType
        request.relativeUrl = relativeUrl
        request.parameters = parameters
        request.headers = headers
        request.httpMethod = httpMethod
        return request
    }

    companion object {
        fun parse(baseUrl: String, method: Method, args: Array<Any>): MethodParser {
            return MethodParser(baseUrl, method, args)
        }
    }
}