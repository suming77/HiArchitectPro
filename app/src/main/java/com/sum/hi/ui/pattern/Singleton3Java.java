package com.sum.hi.ui.pattern;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/18 01:20
 * @类描述 ${TODO}静态内部类单例模式
 * 既能保证线程安全，又能保证唯一性，还能实现单例延时实例化
 * 外部类Singleton3Java被加载的时候，并不会立即加载内部类SingletonProvider，也就不会实例化instance
 * 因此不会占用空间，只有调用getInstance()访问SingletonProvider才会实例化，才会创建Singleton3Java实例
 */
class Singleton3Java {
    private Singleton3Java() {

    }

    //静态内部类里面实例化
    private static class SingletonProvider {
        private static Singleton3Java instance = new Singleton3Java();
    }

    public static Singleton3Java getInstance() {
        return SingletonProvider.instance;
    }
}
