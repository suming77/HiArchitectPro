package com.sum.hi.ui.demo.thread;

import java.util.ArrayList;

/**
 * @Author: smy
 * @Date: 2022/2/24 10:44
 * @Desc:
 *
 * 如果synchronized加在方法上面，未获得对象锁的线程，只能排队，无法访问；
 * 如果synchronized加在代码块上面，未获得对象锁的线程池，只能访问代码块之外的代码，无法访问synchronized代码块内的代码
 * 如果synchronized加载static方法上面，就相对于给Class对象加锁，由于jvm只会存在一份Class对象，所以此时无论是不是同一个java对象，去访问同步方法，都只能排队
 */
public class SynchronizedDemo {
    static ArrayList<String> tickets = new ArrayList();

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            tickets.add("票_" + (i + 1));
        }

        sellTickets();
    }

    static void sellTickets() {
        final SynchronizedTestDemo demo = new SynchronizedTestDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    demo.printThreadName();//同一实力对象方法
//                    new SynchronizedTestDemo().printThreadName();//不同实力对象方法
//                    SynchronizedTestDemo.printThreadName();
//                    new SynchronizedTestDemo().printThreadName();
                }
            }).start();
        }
    }

    static class SynchronizedTestDemo {
        //        synchronized void printThreadName() {
//        static synchronized void printThreadName() {
        void printThreadName() {
            String name = Thread.currentThread().getName();
            System.out.println("买票人" + name + "准备好了…");
            synchronized (this) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                System.out.println("买票人" + name + "正在买票…");
            }

            System.out.println("买票人" + name + "买到票是：" + tickets.remove(0));
        }
    }
}
