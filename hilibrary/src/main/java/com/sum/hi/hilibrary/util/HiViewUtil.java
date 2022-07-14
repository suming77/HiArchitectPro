package com.sum.hi.hilibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/03 00:48
 * @类描述 ${TODO}
 */
public class HiViewUtil {

    /**
     * 获取指定类型的子View
     *
     * @param group viewGroup
     * @param cls   如：RecyclerView.class
     * @param <T>
     *
     * @return 指定类型的View
     */
    public static <T> T findTypeView(@Nullable ViewGroup group, Class<T> cls) {

        if (group == null) {
            return null;
        }
        Deque<View> deque = new ArrayDeque<>();
        deque.add(group);
        while (!deque.isEmpty()) {
            View node = deque.removeFirst();
            if (cls.isInstance(node)) {
                return cls.cast(node);
            } else if (node instanceof ViewGroup) {
                ViewGroup container = (ViewGroup) node;
                for (int i = 0, count = container.getChildCount(); i < count; i++) {
                    deque.add(container.getChildAt(i));
                }
            }
        }
        return null;
    }

    public static boolean isActivityDestroy(Context context) {

        Activity activity = findActivity(context);
        if (activity != null) {
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
                return activity.isDestroyed() || activity.isFinishing();
            }
            return activity.isFinishing();
        }
        return true;
    }

    private static Activity findActivity(Context context) {
        //怎么判断context是不是Activity
        if (context instanceof Activity) {//这种方法不够严谨
            return (Activity) context;
        }

        //ContextWrapper是context的包装类AppcompatActivity，service,application实际上都是ContextWrapper的子类
        //AppcompatXXX类的context都会被包装成TintContextWrapper
        else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
}
