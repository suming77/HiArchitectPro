package com.sum.hi.ui.fragment;

import android.util.Log;

import com.sum.hi.common.component.HiBaseFragment;
import com.sum.hi.ui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
public class FavoriteFragment extends HiBaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }
    @Override
    public void onStart() {

        super.onStart();
        Log.e("TAG", "FavoriteFragment -- onStart: ");
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.e("TAG", "FavoriteFragment -- onResume: ");
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.e("TAG", "FavoriteFragment -- onPause: ");
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.e("TAG", "FavoriteFragment -- onStop: ");
    }
}
