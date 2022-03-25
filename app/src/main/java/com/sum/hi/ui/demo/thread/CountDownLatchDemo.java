package com.sum.hi.ui.demo.thread;

import android.util.Log;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: smy
 * @Date: 2022/3/12 14:38
 * @Desc:演示一个多人过山车 假如有5人一起坐过山车，需要等待5人都准备好才能发车
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(new Random().nextInt(4000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "准备好了");
                    downLatch.countDown();
                }
            }).start();
        }
        downLatch.await();
        System.out.println("所有人都准备好了，准备发车……");
    }
}
