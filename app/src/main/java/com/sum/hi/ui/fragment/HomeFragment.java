package com.sum.hi.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sum.hi.common.component.HiBaseFragment;
import com.sum.hi.ui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
public class HomeFragment extends HiBaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutView.findViewById(R.id.tv_profile).setOnClickListener(v -> {
            navigationView("/profile/detail");
        });
        layoutView.findViewById(R.id.tv_authentication).setOnClickListener(v -> {
            navigationView("/profile/authentication");
        });
        layoutView.findViewById(R.id.tv_vip).setOnClickListener(v -> {
            navigationView("/profile/vip");
        });
        layoutView.findViewById(R.id.tv_unknow).setOnClickListener(v -> {
            navigationView("/profile/unknow");
        });
    }

    private void navigationView(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onStart() {

        super.onStart();
        Log.e("TAG", "HomeFragment -- onStart: ");
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.e("TAG", "HomeFragment -- onResume: ");
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.e("TAG", "HomeFragment -- onPause: ");
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.e("TAG", "HomeFragment -- onStop: ");
    }
}
