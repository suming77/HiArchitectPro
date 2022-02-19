package com.sum.hi.ui.biz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sum.hi.ui.R;
import com.sum.hi.ui.route.RouterFlag;

/**
 * @Author: smy
 * @Date: 2022/2/19 14:53
 * @Desc:
 */
@Route(path = "/profile/authentication", extras = RouterFlag.FLAG_AUTHENTICATION)
public class AuthenticationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
    }
}
