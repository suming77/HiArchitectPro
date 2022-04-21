package com.sum.hi.ui.demo.handler;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sum.hi.hilibrary.User;

/**
 * @Author: smy
 * @Date: 2022/4/5 22:11
 * @Desc:
 */
public class HandlerDemo {
    Handler mHandler = new Handler();

    private void styleRunning() {
        //1.直接在Running中处理任务
        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        //2.使用Handler.Callback来处理消息
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                return true;
            }
        });

        //3.使用HandlerMessage接收处理消息
        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };

//        public Handler() {
//            this(null, false);
//        }
//        public Handler( boolean async){
//            this(null, async);
//        }
//
//        public Handler(@Nullable Callback callback, boolean async){
//            //如果没有调用Looper.prepare则为空
//            // 主线程创建Handler不用创建Looper,就是ActivityThread在进程入口帮我们调用了Looper.prepareMainLooper
//
//            //问题是Looper.myLooper()怎么保证获取到的是本线程的looper对象？
//            mLooper = Looper.myLooper();
//            mQueue = mLooper.mQueue;
//            mCallback = callback;
//            mAsynchronous = async;
//        }
    }

//    public final class Looper {
//        //sThreadLocal是静态变量，可以先简单理解为Map,key是线程，value是Looper
//        //那么你只要使用当前线程的sThreadLocal获取当前线程所属的Looper
//        static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
//        //Looper所属线程的消息队列
//        final MessageQueue mQueue;
//
//        private static void prepare(boolean quitAllowed) {
//            //一个线程只能有一个Looper,prepare不能重复调用
//            if (sThreadLocal.get() != null) {
//                throw new RuntimeException("Only one Looper may be created per thread");
//            }
//            sThreadLocal.set(new android.os.Looper(quitAllowed));
//        }
//
//        private Looper(boolean quitAllowed) {
//            mQueue = new MessageQueue(quitAllowed);
//            mThread = Thread.currentThread();
//        }
//
//        public static @Nullable Looper myLooper() {
//            //具体看ThreadLocal类的get方法
//            //简单理解为map.get(Thread.currentThread())获取当前线程Looper
//            return sThreadLocal.get();
//        }
//    }

    class HandlerThread extends Thread {
        private Handler mHandler;

        @Override
        public void run() {
            Looper.prepare();

            //面试题：如何在子线程中弹出toast
            //Toast.show()
            createHandler();

            Looper.loop();
        }

        private void createHandler() {
            mHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    //处理主线程发送过来的消息
                }
            };
        }
    }

//    class Handler {
//        public final boolean post(@android.annotation.NonNull Runnable r) {
//            return sendMessageDelayed(getPostMessage(r), 0);
//        }
//
//        private static Message getPostMessage(Runnable r) {
//            Message m = Message.obtain();
//            m.callback = r;
//            return m;
//        }
//
//        private boolean enqueueMessage(@NonNull MessageQueue queue, @NonNull Message msg,
//                                       long uptimeMillis) {
//            msg.target = this;
//            ······
//            return queue.enqueueMessage(msg, uptimeMillis);
//        }
//    }

//    class Message {
//        public static final Object sPoolSync = new Object();
//        private static android.os.Message sPool;
//        private static int sPoolSize = 0;
//
//        private static final int MAX_POOL_SIZE = 50;
//
//        public static android.os.Message obtain() {
//            synchronized (sPoolSync) {
//                if (sPool != null) {
//                    android.os.Message m = sPool;
//                    sPool = m.next;
//                    m.next = null;
//                    m.flags = 0; // clear in-use flag
//                    sPoolSize--;
//                    return m;
//                }
//            }
//            return new android.os.Message();
//        }
//
//        void recycleUnchecked() {
//            // Mark the message as in use while it remains in the recycled object pool.
//            // Clear out all other details.
//            flags = FLAG_IN_USE;
//            what = 0;
//            arg1 = 0;
//            arg2 = 0;
//            obj = null;
//            replyTo = null;
//            sendingUid = UID_NONE;
//            workSourceUid = UID_NONE;
//            when = 0;
//            target = null;
//            callback = null;
//            data = null;
//
//            synchronized (sPoolSync) {
//                if (sPoolSize < MAX_POOL_SIZE) {
//                    next = sPool;
//                    sPool = this;
//                    sPoolSize++;
//                }
//            }
//        }
//    }

    //消息入队
//    public final class MessageQueue {
//        boolean enqueueMessage(Message msg, long when) {
//            //普通消息(同步消息和异步消息)的target不能为空，否则不知道交给谁来处理这条消息
//            //这个消息最终被消费的时候，通过handler.dispatchMessage()来完成
//            if (msg.target == null) {
//                throw new IllegalArgumentException("Message must have a target.");
//            }
//
//            synchronized (this) {
//
//                msg.when = when;
//                //mMessage是队头消息，无论何时这个mMessage都会指向队头
//                Message p = mMessages;//mMessages赋值给一个临时变量Message p
//                boolean needWake;
//                //如果队头消息为空，或消息的when=0不需要延迟，或newMsg.when<headMsg.when
//                //则插入队头
//                if (p == null || when == 0 || when < p.when) {
//                    //将msg插入队头，如果被阻塞则唤醒.
//                    msg.next = p;//插入队头非常简单，只需要更改下引用关系就可以了
//                    mMessages = msg;
//                    //如果当前Looper处于休眠状态，则本次插入消息后需要唤醒
//                    needWake = mBlocked;//mBlocked代表当前线程是否处于阻塞状态，
//                    //mBlocked字段只有当队列没有可处理消息或者为空的时候，这个线程才会进入阻塞状态，mBlocked才会为true
//                    //新消息，而是是队头，所以认为是尽可能早执行的，先用needWake记录下来
//                } else {
//                    //插入队列中间，通常我们不必唤醒事件队列，
//                    // 除非队列头部有屏障，并且消息是队列中最早的异步消息
//
//                    //要不要唤醒Looper = 当前Looper处于休眠状态 & 队头是同步屏障消息 & 新消息是异步消息
//                    //目的是为了让异步消息尽早执行
//                    needWake = mBlocked && p.target == null && msg.isAsynchronous();
//                    Message prev;
//                    //本质目的是插入一条新的消息
//                    for (;;) {//无限for循环，但是有break机制
//                        prev = p;
//                        p = p.next;
//                        //按时间顺序 找到改消息 合适的位置
//                        // >>msg1.when=2-->msg.when=4-->msg.when=6
//                        // >>msg1.when=2-->msg.when=4-->【newMsg.when=5】-->msg.when=6
//                        if (p == null || when < p.when) {//找到合适的位置则会退出for循环
//                            break;
//                        }
//                        if (needWake && p.isAsynchronous()) {
//                            needWake = false;
//                        }
//                    }
//                    //调整链表当中节点的引用关系,从而实现消息插入队列的目的
//                    msg.next = p; //p == msg.when=6
//                    prev.next = msg; //msg就是要插入的数据，prev就是msg.when=4
//                }
//
//                //唤醒Looper
//                if (needWake) {
//                    nativeWake(mPtr);//唤醒线程，一旦这个方法被执行，线程就会从阻塞的地方被唤醒过来，继续去工作
//                }
//            }
//            return true;
//        }
//    }

//    //必须使用MessageQueue来发送，msg是无法发送屏障消息的，它只能发送异步消息和同步消息
//    class MessageQueue{
//        //currentTimeMills()系统当前时间，即日期时间，可以被系统设置修改，如果设置系统时间，时间值会发生变化
//        //uptimeMills()自开机后经过的时间，不包括深度休眠时间
//        //sendMessageDelay,postDelay也使用这个时间戳
//        //意思是指，发送了这条消息，在这期间如果设备进入休眠状态，那么消息是不会被执行的设备唤醒之后才会被执行
//        public int postSyncBarrier() {
//            return postSyncBarrier(SystemClock.uptimeMillis());
//        }
//
//        private int postSyncBarrier(long when) {
//            //将新的同步屏障进入队列.
//            //我们不需要唤醒队列，屏障的目的就是阻止它.
//            synchronized (this) {
//                //从消息池复用，构建新消息
//                final Message msg = Message.obtain();
//                //并没有给target字段赋值
//                //区分是不是同步屏障，就看target是否为null
//                //换句话来说，target = null,则消息被认为是同步屏障消息
//                msg.when = when;//对于这个同步屏障消息而言，它的时间只是决定了哪一个位置上面，但是这个消息并不会被执行，
//                //为什么不会的到执行呢？创建Message的时候并没有给target赋值，所以这条消息无法被执行
//
//                Message prev = null;
//                Message p = mMessages;
//                //下面的操作实际上和普通插入消息是一样的，
//                //遍历队列当中所有的消息，直到找到一个message.when>msg.when的消息，决定新消息插入的位置
//                //>>msg1.when=2-->msg.when=4-->msg.when=6
//                //>>msg1.when=2-->msg.when=4-->[newMsg.when=5]-->msg.when=6
//                if (when != 0) {
//                    while (p != null && p.when <= when) {
//                        prev = p;
//                        p = p.next;
//                    }
//                }
//                //如果找到了合适的位置插入
//                if (prev != null) { // invariant: p == prev.next
//                    msg.next = p;
//                    prev.next = msg;
//                } else {
//                    //如果没有找到则直接放弃队头
//                    msg.next = p;
//                    mMessages = msg;
//                }
//                //屏障消息在插入队列的时候是没有主动唤醒线程的，为什么呢？因为屏障消息不需要得到执行，也不需要唤醒线程去轮训它
//                //那么屏障消息不需要被执行，那么它在什么时候从队列当中被移除呢？这个屏障消息是谁添加的就谁来移除
//                //比如ViewRootImpl在接收垂直同步屏障，垂直同步信号的时候，发送一条异步消息，同时发送一条异步屏障，
//                //当这个异步消息得到执行的时候，ViewRootImpl就会把这个同步屏障消息从队列当中移除
//                //面试常问ViewRootImpl是如何保证UI测绘任务有限执行的，是它发送了同步屏障和异步消息
//                return token;
//            }
//        }
//    }

    //消息分发
//    class Looper{
//        public static void loop() {
//            final android.os.Looper me = myLooper();
//            if (me == null) {
//                throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
//            }
//
//            final MessageQueue queue = me.mQueue;
//            ······
//            for (;;) {
//                //无限循环消息驱动器，调用MessageQueue.next获取可执行的消息
//                //might block 可能会被阻塞
//                Message msg = queue.next(); // might block
//                if (msg == null) {
//                    //没有消息.
//                    return;
//                }
//                //分发消息
//                msg.target.dispatchMessage(msg);
//                ·····
//                //回收msg对象，留着复用
//                msg.recycleUnchecked();
//            }
//        }
//    }

//
//    class MessageQueue(){
//
//        Message next() {
//            int nextPollTimeoutMillis = 0;
//            //开启无限for循环消息启动器
//            for (;;) {
//                //如果nextPollTimeoutMillis>0，此时Looper会进入休眠状态，第一次循环由于等于0，所以不会
//                //如果第一次循环没找到需要处理的消息，则nextPollTimeoutMillis会被更新
//                //第二次循环时，如果nextPollTimeoutMillis != 0 looper线程就会进入阻塞状态
//                //在此期间主线程没有实际工作要做，会释放CPU资源，该方法会超时自主恢复，或者插入新消息时被动
//                nativePollOnce(ptr, nextPollTimeoutMillis);
//
//                //当Looepr被唤醒时，会继续向下执行
//                synchronized (this) {
//                    //检索下一条消息，如果找到则返回.
//                    final long now = SystemClock.uptimeMillis();
//                    Message prevMsg = null;
//                    Message msg = mMessages;//队头消息赋值给一个临时变量
//                    //如果队头是屏障消息，则尝试找到一个异步消息
//                    if (msg != null && msg.target == null) {
//                        //屏障消息，查找下一条异步消息.
//                        do {
//                            prevMsg = msg;
//                            msg = msg.next;
//                        } while (msg != null && !msg.isAsynchronous());
//                    }
//                    if (msg != null) {
//                        if (now < msg.when) {
//                            //如果上面找到的消息它的时间，还没到执行的时机
//                            //则更新nextPollTimeoutMillis，也就是下次循环需要阻塞的时间值
//                            // 下一条消息尚未准备好，设置超时待到准备好了唤醒.
//                            nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
//                        } else {
//                            //找到了需要处理的消息.
//                            mBlocked = false;
//                            //由于这个小消息即将被处理，所以需要把它从队列中移除
//                            //通过调节节点的指向关系，达到队列元素移除的目的
//                            if (prevMsg != null) {//说明是被删除的消息是队列中间的某一条消息，prevMsg就是被删除消息的前一个对象
//                                prevMsg.next = msg.next;
//                            } else {//删除的消息是队头消息，那么重新调整一下队头消息，msg就是要被删除的对象
//                                mMessages = msg.next;
//                            }
//                            msg.next = null;
//                            msg.markInUse();
//                            return msg;//返回msg，让它去分发去执行
//                        }
//                    } else {
//                        //如果没有找到消息，即队列为空，looper将进入永久休眠，直到新消息达到.
//                        nextPollTimeoutMillis = -1;
//                    }
//
//                    //处理完待处理的消息后处理退出的消息.
//                    if (mQuitting) {
//                        dispose();
//                        return null;
//                    }
//
//                    // 当队列消息为空，也就是所有任务处理完，或者队头消息已经到达，可执行的时间点
//                    //此时派发通知Looper即将进入空闲状态
//                    if (pendingIdleHandlerCount < 0
//                            && (mMessages == null || now < mMessages.when)) {
//                        pendingIdleHandlerCount = mIdleHandlers.size();
//                    }
//                    if (pendingIdleHandlerCount <= 0) {
//                        // No idle handlers to run.  Loop and wait some more.
//                        mBlocked = true;
//                        continue;
//                    }
//                }
//
//                //注册MessageQueue.IdleHandler，可以监听当前线程的looper是否处于空闲状态，也就意味着当前线程是否
//                //在主线程中可以监听这个事件来延迟初始化，加载数据，日志上报……
//                //而不是有任务就提交，从而避免抢占重要资源
//                for (int i = 0; i < pendingIdleHandlerCount; i++) {
//                    final MessageQueue.IdleHandler idler = mPendingIdleHandlers[i];
//
//                    boolean keep = idler.queueIdle();
//                    if (!keep) {
//                        synchronized (this) {
//                            mIdleHandlers.remove(idler);
//                        }
//                    }
//                }
//
//                // 此时赋值为0，认为既然监听了线程的空闲，那么在这个queueIdle回调里，
//                //很可能又会产生新的消息，为了让消息尽可能早的得到执行，所以此时不需要休眠了
//                nextPollTimeoutMillis = 0;
//            }
//        }
//    }

}
