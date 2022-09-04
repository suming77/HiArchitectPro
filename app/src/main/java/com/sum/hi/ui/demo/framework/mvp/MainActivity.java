package com.sum.hi.ui.demo.framework.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 11:42
 * @类描述 ${TODO}
 */
class MainActivity extends AppCompatActivity implements HomeContract.View {

    private HomePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter();
        //绑定view的关系
        presenter.attach(this);
        presenter.getUserInfo();
    }

    @Override
    public void onGetUserInfo(Object data, String errorMsg) {

    }

    @Override
    public void onSaveUserInfo(Object data, String errorMsg) {

    }

    @Override
    public boolean isAlive() {
        return !isDestroyed() && !isFinishing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除view
        presenter.detach();
    }
}
