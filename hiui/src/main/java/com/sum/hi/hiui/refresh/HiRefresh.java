package com.sum.hi.hiui.refresh;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/05 20:13
 * @类描述 ${TODO}
 */
public interface HiRefresh {
    /**
     * 刷新时禁止滚动
     *
     * @param disableRefreshScroll 是否禁止滚动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置下拉刷新的监听器
     *
     * @param listener 刷新监听器
     */
    void setRefreshListener(HiRefreshListener listener);

    /**
     * 设置下拉刷新的视图
     *
     * @param hiOverView 下拉刷新视图
     */
    void setRefreshOverView(HiOverView hiOverView);

    interface HiRefreshListener {
        void onRefresh();

        boolean enableRefresh();
    }


}
