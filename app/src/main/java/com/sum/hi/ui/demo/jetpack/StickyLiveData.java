package com.sum.hi.ui.demo.jetpack;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展LiveData，支持粘性事件的订阅，分发的StickyLiveData
 *
 * @param <T>
 */
public class StickyLiveData<T> extends LiveData<T> {
    private String mEventName;
    public T mStickyData;
    public int mVersion = 0;
    public ConcurrentHashMap<String, StickyLiveData<T>> mHashMap;

    public StickyLiveData(String eventName) {
        mEventName = eventName;
    }

    /**
     * 调用mVersion++
     * 在我们注册一个Observer的时候，我们需要把它包装一下，目的是为了让Observer的version和LiveData的version对齐
     * 但是LiveData的version字段拿不到，所以需要自己管理version,在对齐的时候使用这个就可以了
     *
     * @param value
     */
    @Override
    protected void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    protected void postValue(T value) {
        mVersion++;
        super.postValue(value);
    }

    /**
     * 发送粘性事件
     * 只能在主线程发送数据
     *
     * @param stickyData
     */
    public void setStickData(T stickyData) {

        this.mStickyData = stickyData;
        setValue(stickyData);
    }

    /**
     * 发送粘性事件，不受线程限制
     *
     * @param stickyData
     */
    public void postStickData(T stickyData) {
        this.mStickyData = stickyData;
        postValue(stickyData);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        observeSticky(owner, observer, false);
    }

    /**
     * 暴露方法，是否关心之前发送的数据,再往宿主上面添加一个addObserver监听生命周期事件，如果是DESTORY则
     * 主动移除LiveData
     *
     * @param owner
     * @param observer
     * @param sticky   是否为粘性事件，sticky=true,如果之前存在已经发送数据，那么者Observer就会收到之前的粘性事件消息
     */
    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer, boolean sticky) {
        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    mHashMap.remove(mEventName);
                }
            }
        });
        super.observe(owner, new LivaDataDemoActivity.StickyObserver<T>(this, (Observer<T>) observer, sticky));
    }
}
