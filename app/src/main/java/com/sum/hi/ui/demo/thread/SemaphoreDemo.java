package com.sum.hi.ui.demo.thread;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @Author: smy
 * @Date: 2022/3/12 15:03
 * @Desc:演示 多人故宫游玩，但是同一时刻限流3人
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3, true);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String name = Thread.currentThread().getName();
                        semaphore.acquire(2);//获取许可证的数量
                        System.out.println(name+"获取到许可证，进去游玩");

                        Thread.sleep(new Random().nextInt(5000));
                        semaphore.release(2);
                        System.out.println(name+"游玩完毕，归还许可证");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
