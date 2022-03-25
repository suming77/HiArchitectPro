package com.sum.hi.ui.demo.thread;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: smy
 * @Date: 2022/2/19 21:52
 * @Desc:
 */
public class ThreadDemoActivity {
    public static void testThread() {
        //传递Runnable
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();


        MyThread myThread = new MyThread();
        myThread.start();
    }

    //复写 Thread#run 方法
    static class MyThread extends Thread {
        @Override
        public void run() {

        }
    }

    public static void testAsyncTask(Context context) {
        class MyAsyncTask extends AsyncTask<String, Integer, String> {

            /**
             * 后台任务执行，进度需要自己来计算
             * @param params
             * @return
             */
            @Override
            protected String doInBackground(String... params) {
                for (int i = 0; i < 10; i++) {
                    publishProgress(i * 10);
                }
                Log.e("smy", "doInBackground == " + params[0]);
                return params[0];
            }

            /**
             * 复写这个方法才能拿到进度
             * @param values
             */
            @Override
            protected void onProgressUpdate(Integer... values) {
                Log.e("smy", "onProgressUpdate == " + values[0]);
            }

            /**
             * 任务执行结果会抛到主线程的onPostExecute方法中
             * @param result
             */
            @Override
            protected void onPostExecute(String result) {
                Log.e("smy", "onPostExecute == " + result);
            }
        }

        //适用于需要知道执行任务进度并更新UI的场景
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("execute MyAsyncTask");

        //所有任务串行执行，先来后到，前面的任务如果休眠或者执行时间过长或者阻塞了，后面的任务都得不到执行
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("smy", "AsyncTask execute(Runnable)");
            }
        });

        //使用内置的THREAD_POOL_EXECUTOR并发执行，适用于并发任务执行
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("smy", "AsyncTask.THREAD_POOL_EXECUTOR");
            }
        });

        //串行执行验证
/*        for (int i = 0; i < 10; i++) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Log.e("smy", "串行" + System.currentTimeMillis());
                }
            });
        }*/
    }

    /**
     * 并没有传递Runnable对象或者复写Runnable方法，来执行任务，因为handler内部自行创建了Looper对象，
     * 能够像主线程中的Looper一样，去轮询自己线程中的消息队列，在使用HandlerThread的时候需要创建一个
     * handler对象，和HandlerThread的Looper相绑定起来，那么在任意位置比如主线程或者其他子线程在使用Handler
     * 向HandlerThread发送消息了。消息最终交给ThreadHandler的handleMessage()这个方法来执行。
     * <p>
     * HandlerThread并不会随着一次任务结束而停止释放资源，它会一直运行着，甚至超过宿主的生命周期。
     * 必要的时候使用quitSafely()或者quit()将其关闭。
     */
    public static void testHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("Handler-Thread");
        handlerThread.start();

        MyHandler handler = new MyHandler(handlerThread.getLooper());

        //这个消息最终会给HandlerThread创建的Looper轮询到,一旦被轮询到就会交给MyHandler来处理，
        handler.sendEmptyMessage(MSG_WHAT_FLAG_1);
        handlerThread.quitSafely();
    }

    private static final int MSG_WHAT_FLAG_1 = 1;

    /**
     * 定义成静态，防止内存泄漏
     */
    static class MyHandler extends Handler {
        public MyHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.e("smy", "MyHandler handleMessage: 接受到处理消息" + msg.what);
            Log.e("smy", "MyHandler handleMessage: currentThreadName == " + Thread.currentThread().getName());
        }
    }

    /**
     * IntentService在onCrate()的时候创建一个线程，用这个线程来执行耗时任务，任务执行完成后就会自我结束。
     */
    public static void testIntentService(Context context) {
        class MyIntentService extends IntentService {
            /**
             * Creates an IntentService.  Invoked by your subclass's constructor.
             *
             * @param name Used to name the worker thread, important only for debugging.
             */
            public MyIntentService(String name) {
                super(name);
            }

            @Override
            protected void onHandleIntent(@Nullable Intent intent) {
                int command = intent.getIntExtra("command", 1);
            }
        }

        context.startService(new Intent());
    }

    public static void testThreadPoolExecutor() {
        Executors.newFixedThreadPool(1);//固定线程数量的线程池
        Executors.newCachedThreadPool();//线程可复用线程池
        Executors.newScheduledThreadPool(10);//可指定定时任务的线程池
        Executors.newSingleThreadExecutor();//线程数量为1线程池
    }

    public static void testThreadPriority() {
        Thread thread = new Thread();
        thread.start();
        int uiPriority = Process.getThreadPriority(0);//输出0
        int priority = thread.getPriority();//输出5

        Log.e("smy", "uiPriority == " + uiPriority + ",priority == " + priority);
    }

    /**
     * 适用于多线程同步，一个线程需要等待另一个线程的执行结果，或者部分结果
     * 保证wait()和notify()的执行顺序，wait()在notify()前执行
     */
    public static void testThreadLock() {
        Thread thread1 = new Thread(new mRunnable1());
        Thread thread2 = new Thread(new mRunnable2());

        /**
         * 谁先执行不确定，谁先抢到资源谁先执行
         * 如果
         */
        thread2.start();
        thread1.start();
    }


    static Object mObject = new Object();
    private static volatile boolean isNotify = false;

    private static class mRunnable1 implements Runnable {
        @Override
        public void run() {
            Log.e("smy", "mRunnable1 == start");
            //只有获取到该资源对象锁的线程才有机会执行同步锁下面的代码块
            synchronized (mObject) {
                try {
                    /**
                     * 属于Object方法，进入wait状态，会让线程1释放对mObject对象锁的持有，
                     * 从而线程2才有机会获取到mObject对象里面的资源锁，执行里面的代码块
                     * wait()方法执行后必须有notify等方法唤醒，否则会造成假死状态，一直无法醒来
                     *
                     * 线程的优先级也只是从概率上面提高线程的调度顺序，但不是必然的，
                     * 解决就这个问题一般配合一个原子变量
                     */
                    if (!isNotify) {
                        mObject.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("smy", "mRunnable1 == end");
            }
        }
    }

    private static class mRunnable2 implements Runnable {
        @Override
        public void run() {
            Log.e("smy", "mRunnable2 == start");
            synchronized (mObject) {
                mObject.notify();//唤醒mRunnable1，但是notify方法并不会让当前线程释放mObject对象资源锁的持有
                isNotify = true;
            }
            Log.e("smy", "mRunnable2 == end");
        }
    }

    /**
     * 一个线程需要等待另一个线程执行完成才能继续的场景
     * join向当前线程插入一条任务
     */
    public static void testThreadJoin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("smy", "testThreadJoin1 == " + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("smy", "testThreadJoin2 == " + System.currentTimeMillis());
            }
        });

        thread.start();
        try {
            //等待thread线程执行完成
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("smy", "testThreadJoin3 == " + System.currentTimeMillis());
    }

    public static void testThreadSleep() {
        Thread thread = new Thread(new Runnable3());
        Thread thread2 = new Thread(new Runnable4());

        thread.start();
        thread2.start();
    }


    static Object mObject2 = new Object();

    static class Runnable3 implements Runnable {

        @Override
        public void run() {
            Log.e("smy", "Runnable3 == " + System.currentTimeMillis());
            synchronized (mObject2) {
                try {
                    /**
                     * sleep()是会释放对象资源锁的，但是在synchronized里面则不会释放，等待执行完才会释放
                     */
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e("smy", "Runnable3 == " + System.currentTimeMillis());
        }
    }

    static class Runnable4 implements Runnable {

        @Override
        public void run() {
            synchronized (mObject2) {
                Log.e("smy", "Runnable4 == " + System.currentTimeMillis());
            }
        }
    }

    static class LooperThread extends Thread {
        private Looper mLooper;

        public LooperThread(String name) {
            super(name);
        }

        public Looper getLooper() {
            synchronized (this) {
                if (mLooper == null && isAlive()) {
                    try {
                        wait();//为空的时候等待一下创建
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return mLooper;
        }

        @Override
        public void run() {
            Looper.prepare();//创建一个Looper
            synchronized (this) {//保存Looper对象
                mLooper = Looper.myLooper();
                notify();//唤醒线程，提供getLooper方法，Looper有可能为空,在执行的时候创建Looper并唤醒线程
            }
            Looper.loop();//开启线程的消息队列循环

        }
    }

    public static void testLooperThread() {
        LooperThread thread = new LooperThread("Looper-Thread");
        thread.start();
        /**
         * 为了让在LooperThread线程中轮询的消息被Handler处理，给Handler绑定一个looper
         */
        Handler handler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.e("smy", "LooperThread == " + msg.what);
                Log.e("smy", "LooperThreadName == " + Thread.currentThread().getName());
                //运行在LooperThread子线程中
            }
        };

        //主线程当中适用Handler发送消息
        handler.sendEmptyMessage(MSG_WHAT_FLAG_1);
    }

    public static void testAtomicInteger() {

        //构建原子类对象
        AtomicInteger atomicInteger = new AtomicInteger(1);

        atomicInteger.getAndIncrement();
        atomicInteger.getAndAdd(2);

        atomicInteger.getAndDecrement();
        atomicInteger.getAndAdd(-2);


    }

    volatile int count;

    public void increment() {
        //其他线程可见
        count = 5;

        //非原子操作，其他线程不可见
        count = count + 1;
        count++;
    }

    /**
     * 一个用原子类修饰，一个用volatile修饰，在多线程的情况下做自增，然后输出最后的值
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        AtomicTask task = new AtomicTask();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    task.incrementAtomic();
                    task.incrementVolatile();
                }
            }
        };

        Thread thread = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread.start();
        thread2.start();

        thread.join();
        thread2.join();

        System.out.println("原子类结果 == " + task.mAtomicInteger.get());//20000
        System.out.println("Volatile类结果 == " + task.volatileCount);//19915
        //说明volatile并不是一个原子性操作
    }

    static class AtomicTask {
        AtomicInteger mAtomicInteger = new AtomicInteger();
        volatile int volatileCount = 0;

        void incrementAtomic() {
            mAtomicInteger.getAndIncrement();
        }

        void incrementVolatile() {
//            volatileCount++;
//            volatileCount = volatileCount + 1;
            volatileCount = 10000;
        }
    }


    /**
     * snychronized
     * 锁Java对象，锁Class对象，锁代码块
     */

    /**
     * 锁方法。加在方法上，未获取到对象锁的其他线程都不可以访问该方法。
     */
    synchronized void printThreadName() {

    }

    /**
     * 锁Class对象。加在static方法上相当于给Class对象枷锁，哪怕是不同的Java对象实例，也需要排队执行
     */
/*    static synchronized void printThreadName() {

    }*/

    /**
     * 锁代码块，未获得对象锁的其他线程可以执行同步代码块之外的代码
     */
/*    void printThreadName() {
        String name = Thread.currentThread().getName();
        System.out.println("线程：" + name + "准备好了…");
        synchronized (this) {

        }
    }*/

}
