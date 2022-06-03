package com.sum.hi.common.component;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/03 23:49
 * @类描述 ${TODO}
 */
public abstract class HiBaseFragment extends Fragment {
    protected View mContentView;

    @LayoutRes
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutId(), container, false);
        return mContentView;
    }

    public void showToast(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }

        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int res) {
        showToast(getContext().getString(res));
    }
}
