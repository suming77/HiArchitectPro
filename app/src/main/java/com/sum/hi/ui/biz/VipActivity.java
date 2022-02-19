package com.sum.hi.ui.biz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sum.hi.ui.route.RouterFlag;

/**
 * @Author: smy
 * @Date: 2022/2/19 14:54
 * @Desc:
 */
@Route(path = "/profile/vip", extras = RouterFlag.FLAG_VIP)
public class VipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
