package com.sum.hi.ui.demo.framework.mvp;

import com.sum.hi.ui.demo.framework.mvp.BasePresenter;
import com.sum.hi.ui.demo.framework.mvp.BaseView;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 10:37
 * @类描述 ${TODO}HomeContract:定义业务接口
 */
public interface HomeContract {
    //统一管理所使用的方法，一目了然
    interface View extends BaseView {
        void onGetUserInfo(Object data, String errorMsg);

        void onSaveUserInfo(Object data, String errorMsg);
    }

    /**
     * 定义一些发起业务或者处理数据逻辑的方法
     * 此外需要另外定义一个HomePresenter去实现它
     */
    abstract class Presenter extends BasePresenter<View> {
        //每个接口都会在View 存在与之对应的callback回调
        public abstract void getUserInfo();

        public abstract void saveUserInfo();
    }

    //model
}
