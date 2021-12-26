package com.sum.hi.hiui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sum.hi.hilibrary.util.HiDisplayUtil;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/05 20:21
 * @类描述 ${TODO}
 */
public abstract class HiOverView extends FrameLayout {

    public enum HiRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,
        /**
         * Header展示状态
         */
        STATE_VISIBLE,

        /**
         * 刷新中的状态
         */
        STATE_REFRESH,

        /**
         * 超出刷新位置松开手后状态
         */
        STATE_OVER_RELEASE,

        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER

    }

    protected HiRefreshState mState = HiRefreshState.STATE_INIT;

    /**
     * 触发下拉刷新的最小高度
     *
     * @param context
     */
    public int mPullRefreshHeight;

    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;

    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;


    public HiOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    private void preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(66, getResources());
        init();
    }


    /**
     * 初始化
     */
    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);


    /**
     * 显示Overlay
     */
    protected abstract void onVisible();

    /**
     * 超过Overlay,释放会加载
     */
    public abstract void onOver();

    /**
     * 开始刷新
     */
    public abstract void onRefresh();

    /**
     * 加载完成
     */
    public abstract void onFinish();

    /**
     * 设置状态
     *
     * @param state
     */
    public void setState(HiRefreshState state) {
        this.mState = state;
    }

    /**
     * 获取状态
     *
     * @return
     */
    public HiRefreshState getState() {
        return mState;
    }
}
