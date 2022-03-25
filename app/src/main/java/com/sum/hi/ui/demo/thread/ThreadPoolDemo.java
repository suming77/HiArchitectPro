package com.sum.hi.ui.demo.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: smy
 * @Date: 2022/2/26 17:29
 * @Desc:
 */
public class ThreadPoolDemo {
    private void test() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                100,
                10,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<>());

//        executor.execute(new Runnable());

//        public ThreadPoolExecutor(int corePoolSize,//核心线程数，默认一直存在线程池中，不会销毁，除非手动销毁，否则这些核心线程，一旦创建则一直存在线程池中
//        int maximumPoolSize,//最大能创建的线程数
//        long keepAliveTime,//非核心线程最大存活时间，如果为0则任务执行完成就会直接销毁，也正是因为这个参数，线程池中的线程在执行完一个任务之后呢，才能够继续存活，继续执行其他的任务，从而达到了线程复用的目的。
//        TimeUnit unit,//keepAliveTime的时间单位
//        BlockingQueue<Runnable> workQueue, //等待队列，当任务提交时，如果线程池中的线程数量大于等于corePoolSize的时候，把该任务放入等待队列。注意是正在运行的线程的数量大于或等于corePoolSize，我们再执行任务时就会把任务添加到队列中等待，如果队列时有界队列，队列容量有限，队列满了之后就会去创建非核心线程。
//        ThreadFactory threadFactory,//线程创建工厂，默认使用 Executors.defaultThreadFactory(),来创建线程，线程具有相同的NORM_PRIORITY优先级并非守护线程。
//        RejectedExecutionHandler handler) //线程池的饱和拒绝策略.如果阻塞队列满了并且没有空闲线程，这时如果继续提交任务，就需要采取一种策略处理该任务。

/*        //单一线程数，同时只有一个线程存活，但是线程等待队列无界
        Executors.newSingleThreadExecutor();
        //线程可复用线程池，核心线程数为0，最大可创建的线程数为Integer.max,线程复用存活时间时60s
        Executors.newCachedThreadPool();
        //固定线程数量的线程池
        Executors.newFixedThreadPool(int nThreads);
        //可执行定时任务，延迟任务的线程池
        Executors.newScheduledThreadPool(int corePoolSize);*/

/*        //提交任务，交由线程池调度
        void execute(Runnable run)
        //关闭线程池，等待任务执行完成
        void shutdown()
        //立即关闭线程池，不等待任务完成
        void shutdownNow()
        //返回线程池中所有任务数量
        int getTaskCount()
        //返回线程池中已执行完成的任务数量
        int getCompletedTaskCount()
        //返回线程池中已创建线程数量
        int getPoolSize()
        //返回当前正在运行的线程数量
        int getActiveCount()*/
    }

}
