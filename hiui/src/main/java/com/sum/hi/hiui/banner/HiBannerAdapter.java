package com.sum.hi.hiui.banner;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sum.hi.hiui.banner.core.HiBannerMo;
import com.sum.hi.hiui.banner.core.IHiBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/26 20:56
 * @类描述 ${TODO}
 * HiViewPager的适配器，为页面填充数据
 */
public class HiBannerAdapter extends PagerAdapter {
    private Context mContext;
    private SparseArray<HiBannerViewHolder> mCacheViews = new SparseArray<>();
    private IHiBanner.OnBannerClickListener mOnBannerClickListener;
    private IBindAdapter mBindAdapter;
    private List<? extends HiBannerMo> mModels;

    /**
     * 是否可以自动轮播
     */
    private boolean mAutoPlay = true;

    /**
     * 非自动轮播状态下是否可以循环切换
     */
    private boolean mLoop = false;
    private int mLayoutResId = -1;

    public HiBannerAdapter(Context context) {
        this.mContext = context;
    }

    private void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        this.mModels = models;
        //数据初始化
        initCacheView();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //无限轮播
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    /**
     * 获取Banner页面数量
     *
     * @return
     */
    public int getRealCount() {
        return mModels == null ? 0 : mModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }

        HiBannerViewHolder hiBannerViewHolder = mCacheViews.get(realPosition);
        if (container.equals(hiBannerViewHolder.rootView.getParent())) {
            container.removeView(hiBannerViewHolder.rootView);
        }

        //数据绑定
        onBind(hiBannerViewHolder, mModels.get(position), position);
        if (hiBannerViewHolder.rootView.getParent() != null) {
            ((ViewGroup) hiBannerViewHolder.rootView.getParent()).removeView(hiBannerViewHolder.rootView);
        }

        container.addView(hiBannerViewHolder.rootView);
        return hiBannerViewHolder.rootView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让item每次都会刷新
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    protected void onBind(@NonNull final HiBannerViewHolder holder, @NonNull final HiBannerMo mo, final int position) {
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBannerClickListener != null) {
                    mOnBannerClickListener.onBannerClick(holder, mo, position);
                }
            }
        });
        if (mBindAdapter != null) {
            mBindAdapter.onBind(holder, mo, position);
        }
    }

    private void initCacheView() {
        mCacheViews = new SparseArray<>();
        for (int i = 0; i < mModels.size(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCacheViews.put(i, viewHolder);
        }
    }

    private View createView(LayoutInflater inflater, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set setLayoutResId first");
        }

        return inflater.inflate(mLayoutResId, parent, false);
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.mBindAdapter = bindAdapter;
    }

    public void setOnBannerClickListener(IHiBanner.OnBannerClickListener listener) {
        this.mOnBannerClickListener = listener;
    }

    public static class HiBannerViewHolder {
        private SparseArray<View> sparseViews;
        View rootView;

        public HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {

            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.sparseViews == null) {
                this.sparseViews = new SparseArray<>();
            }

            V childView = (V) sparseViews.get(id);
            if (childView == null) {
                childView = findViewById(id);
                this.sparseViews.put(id, childView);
            }
            return childView;
        }
    }
}
