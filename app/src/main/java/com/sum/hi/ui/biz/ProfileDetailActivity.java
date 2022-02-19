package com.sum.hi.ui.biz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sum.hi.ui.route.RouterFlag;

/**
 * @Author: smy
 * @Date: 2022/2/19 14:33
 * @Desc:
 */
//如果还需要vip则通过|添加标记
@Route(path = "/profile/detail", extras = RouterFlag.FLAG_LOGIN /*| RouterFlag.FLAG_VIP*/)
public class ProfileDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
