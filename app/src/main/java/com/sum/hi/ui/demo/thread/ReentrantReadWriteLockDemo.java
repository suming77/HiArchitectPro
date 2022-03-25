package com.sum.hi.ui.demo.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: smy
 * @Date: 2022/2/26 14:20
 * @Desc:利用ReentrantReadWriteLock实现多人文档查看于编辑
 */
public class ReentrantReadWriteLockDemo {

    static class ReentrantReadWriteLockTask {
        ReentrantReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();
        private final ReentrantReadWriteLock.ReadLock mReadLock;
        private final ReentrantReadWriteLock.WriteLock mWriteLock;

        public ReentrantReadWriteLockTask() {
            mReadLock = mReadWriteLock.readLock();
            mWriteLock = mReadWriteLock.writeLock();
        }


        public void read() {
            String name = Thread.currentThread().getName();
            try {
                mReadLock.lock();
                System.out.println(name + ":正在读取文件");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mReadLock.unlock();
                System.out.println(name + ":释放读锁");
            }
        }

        public void write() {
            String name = Thread.currentThread().getName();
            try {
                mWriteLock.lock();
                System.out.println(name + ":正在写入文件");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mWriteLock.unlock();
                System.out.println(name + ":释放写锁");
            }
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockTask task = new ReentrantReadWriteLockTask();

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    task.read();
                }
            }).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    task.write();
                }
            }).start();
        }
    }

    /**
     * 把不必要的方法放到锁之外执行
     */
//    public void syncMethod() {
//        noneLockedCode();//2s
//        synchronized (this) {
//            needLockMethod()//2s
//        }
//        noneLockCode2();//2s
//    }

//    public void doSomethingMethod(){
//        synchronized (lock){
//            //do something
//        }
//        ······
//        //这是还有一些代码，做其他不需要同步的工作，但是很快执行完毕
//        ······
//        synchronized (lock){
//            //do other thing
//        }
//    }
//
//    //整合成一个
//    public void doSomethingMethod(){
//        //进行锁粗化，整合成一次锁请求，释放
//        synchronized (lock){
//            //do something
//            //做其他不需要同步但是很快执行完的工作
//            //do other thing
//        }
//    }
}
