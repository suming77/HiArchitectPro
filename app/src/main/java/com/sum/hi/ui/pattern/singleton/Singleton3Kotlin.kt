package com.sum.hi.ui.pattern.singleton

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/18 01:24
 * @类描述 ${TODO}静态内部类单例模式
 */
class Singleton3Kotlin private constructor() {
    companion object {
        val instance = SingletonProvider.holder
    }

    private object SingletonProvider {
        val holder = Singleton3Kotlin()
    }
}