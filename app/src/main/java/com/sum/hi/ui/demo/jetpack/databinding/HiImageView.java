package com.sum.hi.ui.demo.jetpack.databinding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/03 14:40
 * @类描述 ${TODO}
 */
public class HiImageView extends ImageView {
    public HiImageView(Context context) {
        super(context);
    }

    public HiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 1.需要定义成public static ,使用BindingAdapter注解并标记
     * 2.value中的字段随意添加和方法一一对应即可
     * 3.requireAll代表是否以下三个属性在xml中同时使用才会调用到该方法，
     * 为false的话，只要有一个属性被使用就能调用改方法
     */
    @BindingAdapter(value = {"image_url", "isCircle", "radius"}, requireAll = false)
    public static void setImageUrl(ImageView imageView, String imageUrl, boolean isCircle, int radius) {

    }

    //延迟一帧原理
//    class ViewDataBinding {
//        protected void requestRebind() {
//            synchronized (this) {
//                if (USE_CHOREOGRAPHER) {
//                    mChoreographer.postFrameCallback(mFrameCallback);
//                } else {
//                    mUIThreadHandler.post(mRebindRunnable);
//                }
//            }
//        }
//    }
}
