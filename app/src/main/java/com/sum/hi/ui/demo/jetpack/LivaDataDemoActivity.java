package com.sum.hi.ui.demo.jetpack;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataKt;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import java.util.HashMap;

/**
 * @Author: smy
 * @Date: 2022/4/1 21:59
 * @Desc:
 */
public class LivaDataDemoActivity extends AppCompatActivity {
    MutableLiveData mLiveData = new MutableLiveData<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                //无论页面是否可见，都会去执行刷新页面，IO,更有甚者弹出对话框
            }
        };

        //1.无论当前页面是否可见，这条消息都会被分发，---- 消耗资源
        //2.无论宿主是否存活，这条消息都会被分发。 ---- 内存泄漏
//        handler.sendMessage(msg);

        //先注册一个Observer，然后在onChanged接收数据
        mLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {

            }
        });

//        mLiveData.postValue(data);
        //这条数据不是立刻就被派发了的，还要多种条件的判断，默认是不能跨页面的，只能在当前页面使用

        //1.减少资源占用                               页面不可见时不会派发消息
        //2.确保页面保持最新状态                        页面可见时会立马派发最新一条消息给所有观察者，保证页面最新数据状态
        //如果我们在页面不可见期间，多次调用了postValue，当页面可见的时候以最后一次数据为准，也能够避免中间消息分发的过程
        //3.不需要手动处理生命周期                       避免NPE
        //4.可以打造一款不用反注册不会内存泄漏的消息总线   代EventBus


        //创建两个长得差不多的LiveData对象
        LiveData<Integer> liveData1 = new MutableLiveData<>();
        LiveData<Integer> liveData2 = new MutableLiveData<>();

        //在创建一个聚合类MediatorLiveData
        MediatorLiveData<Integer> mediatorLiveData = new MediatorLiveData<>();
        //分别把上面创建的LiveData添加进去
//        mediatorLiveData.addSource(liveData1, observer);
//        mediatorLiveData.addSource(liveData2, observer);

        Observer observer = new Observer() {
            @Override
            public void onChanged(Object o) {
                //一旦liveData1或者LiveData2发送了数据，observer便能观察到，以便统一处理更新UI
            }
        };

        MutableLiveData<Integer> mData = new MutableLiveData<>();
        //数据转换
        LiveData<String> transformLiveData = Transformations.map(mData, input -> String.valueOf(input));

        //使用转换后生成的transformLiveData去观察数据
        transformLiveData.observe(this, output -> {

        });

        //使用原始的LiveData发送数据
        mData.setValue(10);

//        HiDataBus.with("eventName").observe(lifecycelOwner, sticky, new Observer<String>() {
//            @Override
//            public void onChanged(String data) {
//
//            }
//        });
    }

    /**
     * 包装StickyObserver，有新的消息会回调onChanged方法，从这里判断是否要分发这条消息
     * 这只是完成StickyObserver的包装，用于控制事件的分发与否，但是事件的发送还是依靠LiveData来完成的
     *
     * @param <T>
     */
    static class StickyObserver<T> implements Observer<T> {

        private StickyLiveData<T> mLiveData;
        private Observer<T> mObserver;
        private boolean mSticky;//是否开启粘性事件,为false则只能接受到注册之后发送的消息，如果需要接受粘性事件则传true

        //标记该Observer已经接收几次数据了，过滤老数据防止重复接收
        private int mLastVersion = 0;

        public StickyObserver(StickyLiveData liveData, Observer<T> observer, boolean sticky) {
            //比如先使用StickLiveData发送了一条数据，StickLiveData#version=1
            //那么当我们创建WrapperObserver注册进去的时候
            //需要把它的version和StickLiveData的version保持一致
            mLastVersion = liveData.mVersion;
            this.mLiveData = liveData;
            this.mSticky = sticky;
            this.mObserver = observer;
        }

        @Override
        public void onChanged(T t) {
            if (mLastVersion >= mLiveData.mVersion) {//如果相等则说明没有更新的数据要发送
                //但是如果当前Observer是关系粘性事件的，则分发给他
                if (mSticky && mLiveData.mStickyData != null) {
                    mObserver.onChanged(mLiveData.mStickyData);
                }
                return;
            }

            mLastVersion = mLiveData.mVersion;
            mObserver.onChanged(t);
        }
    }


}
