package com.sum.hi.hiui.refresh;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/06 23:41
 * @类描述 ${TODO}
 */
public class HiGestureDetector implements GestureDetector.OnGestureListener {
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
