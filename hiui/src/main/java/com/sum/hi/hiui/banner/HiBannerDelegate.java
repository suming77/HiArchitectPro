package com.sum.hi.hiui.banner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.sum.hi.hiui.banner.core.HiBannerMo;
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
public class HiBannerDelegate implements IHiBanner {
    private Context mContext;
    private HiBanner mHiBanner;

    public HiBannerDelegate(Context context, HiBanner hiBanner) {

        this.mContext = context;
        mHiBanner = hiBanner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends HiBannerMo> models) {

    }

    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {

    }

    @Override
    public void setHiIndicator(HiIndicator<?> hiIndicator) {

    }

    @Override
    public void setAutoPlay(boolean autoPlay) {

    }

    @Override
    public void setLoop(boolean loop) {

    }

    @Override
    public void setIntervalTime(int intervalTime) {

    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {

    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {

    }

    @Override
    public void setScrollDuration(int duration) {

    }
}
