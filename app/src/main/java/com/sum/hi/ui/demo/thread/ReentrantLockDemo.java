package com.sum.hi.ui.demo.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: smy
 * @Date: 2022/2/25 0:44
 * @Desc:
 */
public class ReentrantLockDemo {
    public void method() {
/*        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
        } finally {
            lock.unlock();
        }

        void lock() // 获取锁，获取不到会阻塞
        boolean tryLock() // 尝试获取锁，成功返回true
        boolean tryLock(3000, TimeUnit.MILLISECONDS) // 在一定时间内去不断尝试获取锁
        void lockInterruptibly(); // 可使用Thread.interrupt打断阻塞状态，退出竞争，让给其他线程*/
    }

    static class ReentrantLockTest {
        ReentrantLock mReentrantLock = new ReentrantLock();

        void buyTicket() {
            String name = Thread.currentThread().getName();
            try {
                mReentrantLock.lock();
                System.out.println(name + "准备好了");
                Thread.sleep(100);
                System.out.println(name + "买好了");

                mReentrantLock.lock();
                System.out.println(name + "又准备好了");
                Thread.sleep(100);
                System.out.println(name + "又买好了");

                mReentrantLock.lock();
                System.out.println(name + "又又准备好了");
                Thread.sleep(100);
                System.out.println(name + "又又买好了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mReentrantLock.unlock();
                mReentrantLock.unlock();
                mReentrantLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockTest test = new ReentrantLockTest();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                test.buyTicket();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }

    ReentrantLock lock = new ReentrantLock();

    public void doWork() {
        try {
            lock.lock();
            doWork(); // 递归调用，使得同一线程多次获得锁
        } finally {
            lock.unlock();
        }
    }

}
