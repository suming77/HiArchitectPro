package com.sum.hi.ui.demo.framework.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sum.hi.hilibrary.User;
import com.sum.hi.ui.R;
import com.sum.hi.ui.databinding.HomeViewmodelBinding;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/08/26 23:31
 * @类描述 ${TODO}
 */
class DataBindingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeViewmodelBinding binding = DataBindingUtil.setContentView(this, R.layout.home_viewmodel);
        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //设置数据完成数据绑定工作
                binding.setUser(user);
            }
        });
    }
}
