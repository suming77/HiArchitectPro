package com.sum.hi.ui.pattern.singleton;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/17 22:36
 * @类描述 ${TODO}单例模式-懒汉式
 * 以时间换空间，懒汉式存在一定风险，在多线程情况下有可能存在多份实例
 */
class Singleton2Java {
    //1.
//    private static Singleton2Java instance;

//    //私有化构造方法
//    private Singleton2Java() {
//
//    }

    /**
     * 1.通过synchronized修饰静态方法，这样就可以实现线程安全，保证多个线程并发调用的时候只有一个线程能进入我们的方法内部，
     * 当这个方法没有调用完成时，其他方法会被隔离在外面，等待里面调用完成才有机会进入，保证实例创建的唯一性。
     * 但是这种方式效率低，不建议使用
     */
//    public synchronized static Singleton2Java getInstance() {
//        if (instance == null) {
//            instance = new Singleton2Java();
//        }
//        return instance;
//    }


    //二.双重校验锁，需要给instance增加volatile修饰
    private volatile static Singleton2Java instance;

    //私有化构造方法
    private Singleton2Java() {

    }

    /**
     * 2.双重检验锁，为了调高效率
     * <p>
     * 当两个线程并发访问，它会遇到同步代码块，因为只能只有一个线程先执行，另一个必须等待，当前线程执行完成之后另一个线程才能执行
     * 比如线程a,b,假如a线程已经进入同步代码块，判断第二个instance == null则创建，
     * 线程b先判断第一个instance == null，进入同步代码块，因为线程a还没有执行完成，所以b在等待，当线程a执行完成后
     * 线程b获得执行权限，进入后第二次判断instance == null，所以不会创建instance实例，保证了唯一性
     *
     * 为什么要加volatile修饰，有一种情况，线程a在自己的工作线程里面创建了instance实例，同时还会同步到主程中，
     * 此时线程a已经跳出了代码块，这时候线程b进来了，判断instance == null，就会创建instance，在某些情况下会多次创建instance
     * 也就是说双重校验模式并不能百分百保证唯一性。因为内存空间会被重排，所以增加volatile修饰保证instance的可见性，
     * 防止这种情况出现。
     *
     * 注意：这里需要对instance增加volatile修饰
     */
    public static Singleton2Java getInstance() {
        //避免在instance已经创建的情况下，避免进入同步代码块，提高程序并发效率
        if (instance == null) {
            synchronized (Singleton2Java.class) {
                //保证唯一性
                if (instance == null) {
                    instance = new Singleton2Java();
                }
            }
        }
        return instance;
    }
}
