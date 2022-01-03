package com.sum.hi.hiui.banner;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.sum.hi.hiui.R;
import com.sum.hi.hiui.banner.core.HiBannerMo;
import com.sum.hi.hiui.banner.core.HiCircleIndicator;
import com.sum.hi.hiui.banner.core.HiIndicator;
import com.sum.hi.hiui.banner.core.IHiBanner;

import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/27 00:44
 * @类描述 ${TODO}
 * HiBanner控制器
 * 辅助HiBanner完成各种功能
 * 将HiBanner的一些逻辑内聚在这里，保证暴露给使用这HiBanner干净整洁
 */
public class HiBannerDelegate implements IHiBanner, ViewPager.OnPageChangeListener {
    private Context mContext;
    private HiBanner mHiBanner;

    private HiBannerAdapter mAdapter;
    private HiIndicator<?> mHiIndicator;
    private boolean mAutoPlay;
    private boolean mLoop;
    private List<? extends HiBannerMo> mHiBannerMos;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private IHiBanner.OnBannerClickListener mOnBannerClickListener;
    private HiViewPager mViewPager;

    private int mScrollDuration = -1;

    public HiBannerDelegate(Context context, HiBanner hiBanner) {

        this.mContext = context;
        mHiBanner = hiBanner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends HiBannerMo> models) {
        this.mHiBannerMos = models;
        init(layoutResId);
    }

    public void setAdapter(HiBannerAdapter adapter) {
        this.mAdapter = adapter;
    }

    private void init(int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new HiBannerAdapter(mContext);
        }

        if (mHiIndicator == null) {
            mHiIndicator = new HiCircleIndicator(mContext);
        }

        mHiIndicator.onInflate(mHiBannerMos.size());
        mAdapter.setLayoutResId(layoutResId);
        mAdapter.setBannerData(mHiBannerMos);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setOnBannerClickListener(mOnBannerClickListener);

        mViewPager = new HiViewPager(mContext);
        mViewPager.setIntervalTime(mIntervalTime);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAutoPlay(mAutoPlay);
        mViewPager.setAdapter(mAdapter);
        if (mScrollDuration > 0) mViewPager.setScrollDuration(mScrollDuration);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mViewPager.setAdapter(mAdapter);

        if ((mLoop || mAutoPlay) && mAdapter.getRealCount() != 0) {
            //无限轮播关键点，使用第一张能反向滑动到最后一张，以达到无限轮播的效果
            int firstItem = mAdapter.getFirstItem();
            mViewPager.setCurrentItem(firstItem, false);
        }

        //每次调用数据都会调init方法，清除所有view
        mHiBanner.removeAllViews();
        mHiBanner.addView(mViewPager, layoutParams);
        mHiBanner.addView(mHiIndicator.get(), layoutParams);


    }

    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        setBannerData(R.layout.hi_banner_item_image, models);

    }

    @Override
    public void setHiIndicator(HiIndicator<?> hiIndicator) {
        this.mHiIndicator = hiIndicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (mAdapter != null) mAdapter.setAutoPlay(autoPlay);
        if (mViewPager != null) mViewPager.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
        if (mViewPager != null && duration > 0) mViewPager.setScrollDuration(duration);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mOnPageChangeListener && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }

        position = position % mAdapter.getRealCount();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mHiIndicator != null) {
            mHiIndicator.onPointChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
