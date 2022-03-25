package com.sum.hi.hilibrary.executor

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * @Author:   smy
 * @Date:     2022/2/27 10:22
 * @Desc:支持按照任务的优先级去执行，
 * 支持线程池暂停，恢复（批量文件下载，上传），
 * 异步结果主动回掉主线程的任能力
 */
object HiExecutor {
    private val TAG: String = "HiExecutor"
    private var mHiExecutor: ThreadPoolExecutor
    private var mReentrantLock: ReentrantLock
    private var mCondition: Condition
    private var mIsPaused = false
    private var mMainHandler: Handler = Handler(Looper.getMainLooper())

    init {
        mReentrantLock = ReentrantLock()
        mCondition = mReentrantLock.newCondition()

        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cpuCount + 1
        val maxPoolSize = corePoolSize * 2 + 1
        val blockingDeque: PriorityBlockingQueue<out Runnable> = PriorityBlockingQueue()
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS

        val seq = AtomicLong()
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "hi-executor-" + seq.getAndIncrement()
            return@ThreadFactory thread
        }

        mHiExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingDeque as BlockingQueue<Runnable>,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
//                super.beforeExecute(t, r)
                Log.e(TAG, "${t?.name}, mIsPaused：$mIsPaused")
                if (mIsPaused) {
                    mReentrantLock.lock()
                    try {
                        mCondition.await()//当前线程进入阻塞状态，等待唤醒
                    } finally {
                        mReentrantLock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                //监控线程池消耗任务，线程创建数量，正在运行的数量
                Log.e(TAG, "已执行完的任务优先级：${(r as PriorityRunnable).priority}")
            }
        }
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            mMainHandler.post { onPrepare() }
            val t: T = onBackground()
            mMainHandler.post {
                onComplete(t)
            }
        }

        open fun onPrepare() {
            //比如加载框
        }

        abstract fun onBackground(): T
        abstract fun onComplete(t: T)
    }

    /**
     * 添加@JvmOverloads注解
     * priority参数就可以选择性传递了
     */
    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        mHiExecutor.execute(PriorityRunnable(priority, runnable))
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Callable<*>) {
        mHiExecutor.execute(PriorityRunnable(priority, runnable))
    }

    /**
     * 优先级比较功能
     */
    class PriorityRunnable(val priority: Int, val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return if (this.priority < other.priority) 1 else if (this.priority > other.priority) -1 else 0
        }

    }

    /**
     * 有可能发生在多线程里面，给方法加同步，kotlin可以通过 @Synchronized 注解添加同步锁
     */
    @Synchronized
    fun pause() {
        mReentrantLock.lock()
        try {
            if (mIsPaused) return
            mIsPaused = true
        } finally {
            mReentrantLock.unlock()
        }
        Log.e(TAG, "hiexecutor is paused")
    }

    @Synchronized
    fun resume() {
        mReentrantLock.lock()
        try {
            if (!mIsPaused) return
            mIsPaused = false
            mCondition.signalAll()//唤醒所有线程
        } finally {
            mReentrantLock.unlock()
        }
        Log.e(TAG, "hiexecutor is resumed")
    }
}