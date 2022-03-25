package com.sum.hi.ui.demo.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: smy
 * @Date: 2022/2/25 22:10
     * @Desc: 多个线程去打印纸张，每个线程打印两张(ReentrantLock 公平锁，非公平锁)
     * 场景：公平锁：交易
     * 非公平锁：synchronized
 */
public class ReentrantLockDemo2 {

    static class ReentrantLockTask {
        ReentrantLock mReentrantLock = new ReentrantLock(false);

        void print() {
            try {
                String name = Thread.currentThread().getName();

                mReentrantLock.lock();
                System.out.println(name + ":第一次打印");
                Thread.sleep(1000);
                mReentrantLock.unlock();//释放锁

                //在公平锁的情况下，这个锁更倾向与给与等待队列当中的线程，也就是说在公平锁的情况下，等待队列当中的线程都有机会获取这把锁去执行他的任务
                //在非公平锁当中，当一个线程释放掉锁之后它立刻去申请锁，此时它会优先获得这个锁，因为等待队列中的线程都是处于阻塞状态的。
                //我们知道唤醒一个阻塞状态的线程是需要再次恢复当时的状态的，这会带来更大的开销，而当前这个线程只是释放掉了，
                //还没有进入阻塞状态，把锁交给当前线程执行是最佳选择，当前开销也是最小的。非公平锁的性能远远高于公平锁，
                //就是因为恢复一个挂起的线程需要很大的开销和时间的，而非公平锁能更加充分利用CPU的时间片，减少CPU的空闲时间
                //非公平锁的缺点就是，导致线程饿死，一直得不到执行
                mReentrantLock.lock();//再次尝试获取锁
                System.out.println(name + ":第二次打印");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mReentrantLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockTask task = new ReentrantLockTask();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                task.print();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }
}
