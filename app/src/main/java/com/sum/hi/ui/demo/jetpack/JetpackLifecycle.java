package com.sum.hi.ui.demo.jetpack;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ReportFragment;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * @Author: smy
 * @Date: 2022/3/31 14:12
 * @Desc:
 */
public class JetpackLifecycle {
    //第一种：
    //1.自定义LifecycleObserver观察者，用注解声明没个方法观察宿主的状态
    public class MyLifecycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void onStart(@NotNull LifecycleOwner owner) {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void onStop(@NotNull LifecycleOwner owner) {

        }
    }

    //2.注册观察者，观察宿主生命周期状态变化
    class MyFragment1 extends androidx.fragment.app.Fragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            MyLifecycleObserver observer = new MyLifecycleObserver();
            getLifecycle().addObserver(observer);
        }
    }

    //第二种：
//    interface FullLifecycleObserver extends LifecycleObserver {
//        void onCreate(LifecycleOwner owner);
//
//        void onStart(LifecycleOwner owner);
//
//        void onResume(LifecycleOwner owner);
//
//        void onPause(LifecycleOwner owner);
//
//        void onStop(LifecycleOwner owner);
//
//        void onDestory(LifecycleOwner owner);
//    }
//
//    class MyLifecycleObserver extends FullLifecycleObserver {
//
//        void onStart(LifecycleOwner owner) { }
//
//        void onStop(LifecycleOwner owner) { }
//    }

    //第三种：
//    public interface LifecycleEventObserver extends LifecycleObserver {
//        void onStateChanged(LifecycleOwner owner, Lifecycle.Event event);
//    }
//
//    class MyLifecycleObserver2 extends LifecycleEventObserver {
//        @Override
//        public void onStateChanged(LifecycleOwner owner, Lifecycle.Event event) {
//            //需要自行判断lifeEvent是onStart还是onStop
//        }
//    }


    /**
     * Fragment实现Lifecycle
     */
    public class Fragment implements LifecycleOwner {
        LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

        /**
         * 说明Lifecycle是LifecycleRegistry的父类，这是一种面向接口的编程思想，我们注册 Lifecycle的时候，
         * 实际上都注册到LifecycleRegistry里面去了
         *
         * @return
         */
        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            //复写自LifecycleOwner,所以必须new LifecycleRegistry对象返回
            return mLifecycleRegistry;
        }

        void performCreate() {
            //分发自己的状态到每个观察者，从而实现观察生命周期实现变化的能力
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }

        void performStart() {
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        }

        void performResume() {
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        }
    }

    /**
     * Activity实现Lifecycle
     */
    public class ComponentActivity extends Activity implements LifecycleOwner {
        private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //往Activity上添加一个fragment，用以报告生命周期的变化
            //目的是为了兼顾不是继承自AppCompactActivity的场景
            ReportFragment.injectIfNeededIn(this);
        }
    }

    /**
     * ReportFragment的核心源码
     */
//    public class ReportFragment extends androidx.fragment.app.Fragment {
//        public static void injectIfNeededIn(Activity activity) {
//            FragmentManager manager = activity.getFragmentManager();
//            if (manager.findFragmentByTag(REPORT_FRAGMENT_TAG) == null) {
//                manager.beginTransaction().add(new ReportFragment(), REPORT_FRAGMENT_TAG).commit();
//                manager.executePendingTransactions();
//            }
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            dispatch(Lifecycle.Event.ON_START);
//        }
//
//        @Override
//        public void onResume() {
//            super.onResume();
//            dispatch(Lifecycle.Event.ON_RESUME);
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            dispatch(Lifecycle.Event.ON_PAUSE);
//        }
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            dispatch(Lifecycle.Event.ON_DESTROY);
//        }
//
//        private void dispatch(Lifecycle.Event event) {
//            Lifecycle lifecycle = getActivity().getLifecycle();
//            if (lifecycle instanceof LifecycleRegistry) {
//                ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
//            }
//        }
//    }

        //单例类
    class AppLifecycleOwner implements LifecycleOwner{
        private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        void init(Application application){
            //利用application的ActivityLifecycleCallbacks 去箭听每个Activity的onStart,onstop事件
            //计算可见的Activity数量，从而计算出当前处于前台还是后台，然后分发给每个观察者。

        }
    }

}
