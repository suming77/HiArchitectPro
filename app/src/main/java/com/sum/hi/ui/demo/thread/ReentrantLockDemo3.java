package com.sum.hi.ui.demo.thread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: smy
 * @Date: 2022/2/25 23:38
 * @Desc: 演示生产者于消费者的场景，利用的是ReentrantLock condition条件对象，能够指定唤醒某个线程去工作
 * 生产者是一个boss，去生产砖，奇数砖让工人1去搬，偶数砖让工人2去搬
 * 消费者是两个工人，有砖就搬，无砖则休息
 */
public class ReentrantLockDemo3 {

    static class ReentrantLockTask {
        ReentrantLock mReentrantLock = new ReentrantLock(true);
        private final Condition mWorker1Condition;
        private final Condition mWorker2Condition;

        public ReentrantLockTask() {
            mWorker1Condition = mReentrantLock.newCondition();
            mWorker2Condition = mReentrantLock.newCondition();
        }

        volatile int flag = 0;

        void work1() {
            try {
                mReentrantLock.lock();
                if (flag == 0 || flag % 2 == 0) {
                    System.out.println("工人1无砖可搬，休息会~");
                    mWorker1Condition.await();//进入阻塞状态,等待唤醒
                }
                System.out.println("工人1搬砖是" + flag);
                flag = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mReentrantLock.unlock();
            }
        }

        void work2() {
            try {
                mReentrantLock.lock();
                if (flag == 0 || flag % 2 != 0) {
                    System.out.println("工人2无砖可搬，休息会~");
                    mWorker2Condition.await();//进入阻塞状态，等待唤醒
                }
                System.out.println("工人2搬砖是" + flag);
                flag = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mReentrantLock.unlock();
            }
        }

        void boss() {
            try {
                mReentrantLock.lock();
                flag = new Random().nextInt(100);
                if (flag % 2 == 0) {
                    mWorker2Condition.signal();//指定换醒线程2
                    System.out.println("生产出来砖，唤醒让工人2去搬：" + flag);
                } else {
                    mWorker1Condition.signal();//指定唤醒线程1
                    System.out.println("生产出来砖，唤醒让工人1去搬：" + flag);
                }
            } finally {
                mReentrantLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockTask task = new ReentrantLockTask();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    task.work1();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    task.work2();
                }
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            task.boss();
        }

        System.out.println("偶数" + (58 % 2));
        System.out.println("奇数" + (59 % 2));
    }


}
