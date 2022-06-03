package com.sum.hi.ui.demo.handler;

import android.util.Log;

import androidx.annotation.Nullable;

import com.sum.hi.hilibrary.User;

import java.lang.ref.WeakReference;

/**
 * @Author: smy
 * @Date: 2022/4/10 21:14
 * @Desc:
 */
public class ThreadLocalDemo {
    public void ThreadLocal() {
        /**
         * 假如里面调用多个方法，需要传递同一个或者多个参数，则需要把一个个参数传递过去，
         * 实际上这些参数是没必要去 传递的，我们只需要在任意方法里面通过ThreadLocal取值就可以啦
         */
//        HiExecutor.INSTANCE.execute(new Runnable() {
//            @Override
//            public void run() {
//                method1();
//                method2();
//                method3();
//                method4();
//            }
//        });

        User user = new User("haha");
        ThreadLocal threadLocal = new ThreadLocal<User>() {
            @Nullable
            @Override
            protected User initialValue() {
                return user;
            }
        };


        //开两个线程去操作threadLocal的user对象，那么每个线程会对user对象进行一次拷贝，进行数据修改之后
        //去验证修改后的数据会同步到主线程中的user对象，会不会影响到别的线程的user对象值
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User mUser = (User) threadLocal.get();
                    mUser.setName("新名字" + finalI);
                }
            }).start();
        }

        Log.e("smy", "ThreadLocal user name ==" + threadLocal.get());//新名字1
        Log.e("smy", "主线程 user name ==" + user.getName());//新名字1
        //Thread中修改threadLocal的user中的name后，会同步到主线程中的user对象中，会影响到别的线程的user对象的值
        //所以threadLocal不具备线程安全的能力，也不具备线程间数据隔离的能力，它仅仅是简化了参数的传递，方便在线程的任意地方获取数据

    }
//
//    class ThreadLocal {
//        public void set(T value) {
//            //获取当前线程的ThreadLocalMap对象
//            Thread t = Thread.currentThread();
//            ThreadLocalMap map = getMap(t);
//            if (map != null)
//                map.set(this, value);//更新值，key=threadLocal
//            else
//                createMap(t, value);//创建并设置值
//        }
//
//        void createMap(Thread t, T firstValue) {
//            //每一个线程都有一个ThreadLocalMap对象，key为ThreadLocal
//            t.threadLocals = new ThreadLocalMap(this, firstValue);
//        }
//
//        public T get() {
//            Thread t = Thread.currentThread();
//            ThreadLocalMap map = getMap(t);
//            if (map != null) {
//                ThreadLocalMap.Entry e = map.getEntry(this);
//                if (e != null) {
//                    @SuppressWarnings("unchecked")
//                    T result = (T) e.value;
//                    return result;
//                }
//            }
//            return setInitialValue();
//        }
//
//        private T setInitialValue() {
//            //得到被调用的threadlocal的初始值(对应上文中的主线程threadLocal对象)
//            T value = initialValue();
//            //得到当前线程，对应线程池的每个线程
//            Thread t = Thread.currentThread();
//            //获取或创建每个线程的ThreadLocalMap,并把初始值存放进去
//            ThreadLocalMap map = getMap(t);
//            if (map != null)
//                map.set(this, value);
//            else
//                createMap(t, value);
//            return value;
//        }
//    }
//
//    static class ThreadLocalMap {
//        //本质是一个数组
//        private Entry[] table;
//
//        static class Entry extends WeakReference<java.lang.ThreadLocal<?>> {
//
//            Object value;
//
//            Entry(java.lang.ThreadLocal<?> k, Object v) {
//                //key被弱引用持有
//                super(k);
//                //value被强引用赋值
//                value = v;
//            }
//        }
//
//        private void set(java.lang.ThreadLocal<?> key, Object value) {
//
//            Entry[] tab = table;
//            int len = tab.length;
//            //获取Key的hash散列值，使得value均价分布在table[]数组内
//            int i = key.threadLocalHashCode & (len - 1);
//            ······
//            replaceStaleEntry(key, value, i);//移除key=null的元素
//            ······
//            tab[i] = new Entry(key, value);
//            ······
//        }
//    }
}
