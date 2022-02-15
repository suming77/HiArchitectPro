package com.sum.hi.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sum.hi.common.component.HiBaseFragment;
import com.sum.hi.ui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 16:22
 * @类描述 ${TODO}
 */
public class CategoryFragment extends Fragment {

//    @Override
//    protected int getLayoutId() {
//
//        return R.layout.fragment_category;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e("smy","onCreateView");
        View layoutView = inflater.inflate(R.layout.fragment_category, container, false);
        TextView tv_time = layoutView.findViewById(R.id.tv_time);
        tv_time.setText("CategoryFragment: "+System.currentTimeMillis());
        return layoutView;
    }


}
