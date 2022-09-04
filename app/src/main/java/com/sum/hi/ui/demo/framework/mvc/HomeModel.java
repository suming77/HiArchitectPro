package com.sum.hi.ui.demo.framework.mvc;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sum.hi.hilibrary.User;
import com.sum.hi.hilibrary.annotation.HiCallback;
import com.sum.hi.hilibrary.annotation.HiResponse;
import com.sum.hi.hilibrary.restful.HiRestful;

import org.jetbrains.annotations.NotNull;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 07:54
 * @类描述 ${TODO}mvc模式
 */
class HomeModel implements IHomeModel {
    @Override
    public void getUserInfo(HiCallback<User> callback) {
//        HiRestful.create(User.class).getUserInfo()
//                .enqueue(callback);
    }
}

/*
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeModel model = new HomeModel();
        model.getUserInfo(new HiCallback<User>() {
            @Override
            public void onSuccess(@NotNull HiResponse<User> response) {

            }

            @Override
            public void onFailed(@NotNull Throwable throwable) {

            }
        });
        //HiRestful.create(User.class).getUserInfo().enqueue(callback);
    }
}
*/
