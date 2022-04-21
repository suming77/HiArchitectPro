package com.sum.hi.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sum.hi.common.component.HiBaseFragment;
import com.sum.hi.hilibrary.executor.HiExecutor;
import com.sum.hi.ui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
public class ProfileFragment extends HiBaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    boolean isPause = true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView.findViewById(R.id.tv_thread1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int priority = 0; priority < 10; priority++) {
                    int finalPriority = priority;
                    HiExecutor.INSTANCE.execute(priority, () -> {
                        try {
                            Thread.sleep(1000 - finalPriority * 100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        mContentView.findViewById(R.id.tv_thread2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    HiExecutor.INSTANCE.resume();
                } else {
                    HiExecutor.INSTANCE.pause();
                }
                isPause = !isPause;
            }
        });
        mContentView.findViewById(R.id.tv_thread3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HiExecutor.INSTANCE.execute(new HiExecutor.Callable<String>() {
                    @Override
                    public void onComplete(String result) {
                        Log.e("HiExecutor", "onComplete-当前线程" + Thread.currentThread().getName());
                        Log.e("HiExecutor", "onComplete-结果回调:" + result);
                    }

                    @Override
                    public String onBackground() {
                        Log.e("HiExecutor", "onBackground-当前线程" + Thread.currentThread().getName());
                        return "我是异步任务结果";
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG", "ProfileFragment -- onStart: ");
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.e("TAG", "ProfileFragment -- onResume: ");
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.e("TAG", "ProfileFragment -- onPause: ");
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.e("TAG", "ProfileFragment -- onStop: ");
    }
}
