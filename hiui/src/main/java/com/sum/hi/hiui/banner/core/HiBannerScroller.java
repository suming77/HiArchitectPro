package com.sum.hi.hiui.banner.core;

import android.content.Context;
import android.widget.Scroller;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/02 21:50
 * @类描述 ${TODO}
 */
public class HiBannerScroller extends Scroller {
    /**
     * 值越大，滑动越慢
     */
    private int mDuration = 1000;

    public HiBannerScroller(Context context, int duration) {
        super(context);
        this.mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
