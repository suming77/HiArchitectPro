package com.sum.hi.ui.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sum.hi.hiui.tab.bottom.HiTabBottomInfo;

import java.security.PublicKey;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 15:47
 * @类描述 ${TODO}
 */
public class HiTabViewAdapter {
    private List<HiTabBottomInfo<?>> mBottomInfos;
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;

    public HiTabViewAdapter(List<HiTabBottomInfo<?>> bottomInfos, Fragment fragment, FragmentManager fragmentManager) {
        this.mBottomInfos = bottomInfos;
        this.mCurrentFragment = fragment;
        this.mFragmentManager = fragmentManager;
    }

    public void instanceItem(View container, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }

        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            fragmentTransaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                fragmentTransaction.add(container.getId(), fragment, name);
            }
        }
        mCurrentFragment = fragment;
        fragmentTransaction.commitAllowingStateLoss();//提价本次修改
    }

    public Fragment getItem(int position) {
        try {
            return mBottomInfos.get(position).fragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public int getCount() {
        return mBottomInfos == null ? 0 : mBottomInfos.size();
    }
}
