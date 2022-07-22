package com.sum.hi.ui.pattern.singleton;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/17 20:24
 * @类描述 ${TODO} 单例模式-饿汉模式
 * 在类初始化的时候就创建了，以空间换时间，不存在线程安全问题
 * 缺点，即使没有被用到也会创建，比较占内存
 */
class Singleton1Java {
    private static Singleton1Java instance = new Singleton1Java();

    //构造方法私有化
    private Singleton1Java(){

    }

    public static Singleton1Java getInstance() {
        return instance;
    }
}
