package com.sum.hi.hiui.banner;

import com.sum.hi.hiui.banner.core.HiBannerMo;

/**
 * HiBanner的数据绑定接口，基于该接口可以实现数据的绑定和框架层解耦
 */
public interface IBindAdapter {
    void onBind(HiBannerAdapter.HiBannerViewHolder viewHolder, HiBannerMo mo, int position);
}
