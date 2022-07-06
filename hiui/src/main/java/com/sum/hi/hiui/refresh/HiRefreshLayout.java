package com.sum.hi.hiui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/06 23:30
 * @类描述 ${TODO}
 */
public class HiRefreshLayout extends FrameLayout implements HiRefresh {
    private HiOverView.HiRefreshState mState;
    private GestureDetector mGestureDetector;
    private HiRefresh.HiRefreshListener mHiRefreshListener;
    private HiOverView mHiOverView;
    private int mLastY;
    //刷新时禁止滑动
    private boolean disableRefreshScroll;
    private AutoScroller mAutoScroller;

    public HiRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), gestureDetector);
        mAutoScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
       final View head = getChildAt(0);
        mHiOverView.onFinish();
        mHiOverView.setState(HiOverView.HiRefreshState.STATE_INIT);
        final int bottom = head.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = HiOverView.HiRefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshListener(HiRefreshListener listener) {
        this.mHiRefreshListener = listener;
    }

    @Override
    public void setRefreshOverView(HiOverView hiOverView) {
        if (mHiOverView != null) {
            removeView(mHiOverView);
        }

        this.mHiOverView = hiOverView;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mHiOverView, 0, layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //定义head和child的排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == HiOverView.HiRefreshState.STATE_REFRESH) {
                head.layout(0, mHiOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mHiOverView.mPullRefreshHeight);
                child.layout(0, mHiOverView.mPullRefreshHeight, right, mHiOverView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }

            View other;
            for (int i = 2; i < getChildCount(); ++i) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }

    private HiGestureDetector gestureDetector = new HiGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || mHiRefreshListener != null && !mHiRefreshListener.enableRefresh()) {
                return false;//横向滑动，或刷新被禁止不处理
            }
            if (disableRefreshScroll && mState == HiOverView.HiRefreshState.STATE_REFRESH) {//刷新时是否禁止滚动
                return true;
            }

            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            //如果列表发生了滚动则不处理
            if (HiScrollUtil.childScrolled(child)) {
                return false;
            }
            //对于可刷新或者没有达到可刷新距离，且头部已经下拉
            if ((mState != HiOverView.HiRefreshState.STATE_REFRESH
                    || head.getBottom() <= mHiOverView.mPullRefreshHeight)
                    && (head.getBottom() > 0 || distanceY <= 0.0f)) {
                //是否还在滑动中
                if (mState != HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    int seed;
                    //速度计算
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        seed = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        seed = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    //如果是正在刷新状态，则不允许在滑动时改变状态
                    boolean bool = moveDown(seed, true);
                    mLastY = (int) (-distanceY);
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动Header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     * @return
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {//异常情况的补充
            offsetY = -childTop;
            //移动head和child的位置到原始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);

            if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                mState = HiOverView.HiRefreshState.STATE_INIT;
            }
        } else if (mState == HiOverView.HiRefreshState.STATE_REFRESH && childTop > mHiOverView.mPullRefreshHeight) {
                return false;//如果下拉刷新中，禁止继续下拉
            } else if (childTop <= mHiOverView.mPullRefreshHeight) {//还没超出刷新距离
                if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_VISIBLE && nonAuto) {//头部开始显示
                    mHiOverView.onVisible();
                    mHiOverView.setState(HiOverView.HiRefreshState.STATE_VISIBLE);
                    mState = HiOverView.HiRefreshState.STATE_VISIBLE;
                }
                head.offsetTopAndBottom(offsetY);
                child.offsetTopAndBottom(offsetY);
                if (childTop == mHiOverView.mPullRefreshHeight && mState == HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    //下拉刷新完成
                    refresh();
                }
            } else {
                if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_OVER && nonAuto) {
                    //超出刷新位置
                    mHiOverView.onOver();
                    mHiOverView.setState(HiOverView.HiRefreshState.STATE_OVER);
                }
                head.offsetTopAndBottom(offsetY);
                child.offsetTopAndBottom(offsetY);
            }

            if (mHiOverView != null) {
                mHiOverView.onScroll(head.getBottom(), mHiOverView.mPullRefreshHeight);
            }
        return true;
    }

    /**
     * 刷新
     */
    private void refresh() {
        if (mHiRefreshListener != null) {
            mState = HiOverView.HiRefreshState.STATE_REFRESH;
            mHiOverView.onRefresh();
            mHiOverView.setState(HiOverView.HiRefreshState.STATE_REFRESH);
            mHiRefreshListener.onRefresh();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mAutoScroller.isFinished()){
            return false;
        }
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            //松开手
            if (head.getBottom() > 0) {
                if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {//非正在刷新
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if ((consumed || (mState != HiOverView.HiRefreshState.STATE_INIT && mState != HiOverView.HiRefreshState.STATE_REFRESH) && head.getBottom() != 0)) {
            ev.setAction(MotionEvent.ACTION_CANCEL);//让父类接收不到真实的事件
            return super.dispatchTouchEvent(ev);
        }

        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }

    }

    /**
     * 滚动
     *
     * @param distance 滚动距离
     */
    private void recover(int distance) {
        if (mHiRefreshListener != null && distance > mHiOverView.mPullRefreshHeight) {
            //滚动位置
            mAutoScroller.recover(distance - mHiOverView.mPullRefreshHeight);
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(distance);
        }
    }

    /**
     * 借助Scroller完成视图滚动
     */
    private class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroller() {
            this.mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//是否完成滚动
                moveDown(mLastY - mScroller.getCurrY(), false);
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }

            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }
    }
}
