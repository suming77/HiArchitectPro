package com.sum.hi.ui.pattern

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/17 20:25
 * @类描述 ${TODO}
 */
//通过Kotlin对象声明实现饿汉模式
object Singleton1Kotlin {

}

//kotlin - showkotlinbytecode-反编译代码
//public final class Singleton1Kotlin {
//    @NotNull
//    public static final Singleton1Kotlin INSTANCE;
//
//    private Singleton1Kotlin() {
//    }
//
//    static {
//        Singleton1Kotlin var0 = new Singleton1Kotlin();
//        INSTANCE = var0;
//    }
//}