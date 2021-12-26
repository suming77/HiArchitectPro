package com.sum.hi.hiui.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/07 00:33
 * @类描述 ${TODO}
 */
public class HiScrollUtil {
    /**
     * 判断child是否发生了滚动
     *
     * @param child
     * @return true 发生了滚动
     */
    public static boolean childScrolled(@NonNull View child) {
        if (child instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) child;
            if (adapterView.getFirstVisiblePosition() != 0 ||
                    adapterView.getFirstVisiblePosition() == 0 &&
                            adapterView.getChildAt(0) != null &&
                            adapterView.getChildAt(0).getTop() < 0) {
                return true;
            }
        } else if (child.getScrollY() > 0) {
            return true;
        }
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            View childAt = recyclerView.getChildAt(0);
            int position = recyclerView.getChildAdapterPosition(childAt);
            return position != 0 || childAt.getTop() != 0;
        }
        return false;
    }

    /**
     * 查找滚动的child
     *
     * @return 可以滚动的Child
     */
    public static View findScrollableChild(@NonNull ViewGroup viewGroup) {
        View child = viewGroup.getChildAt(1);
        if (child instanceof RecyclerView || child instanceof AdapterView) {
            return child;
        }

        //再往下找一层
        if (child instanceof ViewGroup) {
            View tempChild = ((ViewGroup) child).getChildAt(0);
            if (tempChild instanceof RecyclerView || tempChild instanceof AdapterView) {
                child = tempChild;
            }
        }
        return child;
    }

}
