package com.sum.hi.ui.demo.framework.mvp;

import com.sum.hi.hilibrary.User;
import com.sum.hi.hilibrary.annotation.HiCallback;
import com.sum.hi.hilibrary.annotation.HiResponse;
import com.sum.hi.hilibrary.restful.HiRestful;

import org.jetbrains.annotations.NotNull;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 11:34
 * @类描述 ${TODO}
 */
class HomePresenter extends HomeContract.Presenter {
    @Override
    public void getUserInfo() {
//        HiRestful.create(Home.class).getUserIonf(new HiCallback<User>() {
//            @Override
//            public void onSuccess(@NotNull HiResponse<User> response) {
//                //通过View回传数据
//                if (mView != null && mView.isAlive()) {
//                    mView.onGetUserInfo(response.getData(), null);
//                }
//            }
//
//            @Override
//            public void onFailed(@NotNull Throwable throwable) {
//
//            }
//        });
    }

    @Override
    public void saveUserInfo() {

    }
}
