package com.sum.hi.ui.fragment;

import android.util.Log;

import com.sum.hi.common.component.HiBaseFragment;
import com.sum.hi.ui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
public class RecommendFragment extends HiBaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }
    @Override
    public void onStart() {

        super.onStart();
        Log.e("TAG", "RecommendFragment -- onStart: ");
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.e("TAG", "RecommendFragment -- onResume: ");
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.e("TAG", "RecommendFragment -- onPause: ");
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.e("TAG", "RecommendFragment -- onStop: ");
    }
}
