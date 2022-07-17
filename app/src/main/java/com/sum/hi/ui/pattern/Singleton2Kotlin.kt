package com.sum.hi.ui.pattern

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/17 22:41
 * @类描述 ${TODO}单例模式-懒汉式
 * 1.kotlin中通过使用@Synchronized注解修饰方法，但是这种并发效率比较低，不建议使用这种方式
 */
//私有构造方法,保证不被其他方式创建实例
class Singleton2Kotlin private constructor() {
    //伴生对象
//    companion object {
//        private var instance: Singleton2Kotlin? = null
//            get() {
//                if (field == null) {
//                    field = Singleton2Kotlin()
//                }
//                return field
//            }
//
//        @Synchronized
//        fun get(): Singleton2Kotlin {
//            return instance!!
//        }
//    }

    //2.双重校验锁模式
    companion object {
        val instance: Singleton2Kotlin by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Singleton2Kotlin()
        }
    }

    /**
     * lazy有两个参数：fun <T> lazy(lock: Any?, initializer: () -> T)
     * lock:线程模式， LazyThreadSafetyMode.SYNCHRONIZED线程安全模式，在SynchronizedLazyImpl中实现了
     *
     */
}