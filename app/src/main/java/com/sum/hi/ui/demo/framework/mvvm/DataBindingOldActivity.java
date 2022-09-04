package com.sum.hi.ui.demo.framework.mvvm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.sum.hi.ui.R;
import com.sum.hi.ui.databinding.HomeViewmodelOldBinding;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/08/26 22:18
 * @类描述 ${TODO}
 */
class DataBindingOldActivity extends AppCompatActivity {
    private static final String TAG = "DataBindingOldActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeViewmodelOldBinding binding = DataBindingUtil.setContentView(this, R.layout.home_viewmodel_old);
        final HomeViewModelOld viewModel = new HomeViewModelOld();
        binding.setViewModel(viewModel);

        viewModel.queryUserInfo();

        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged == " + viewModel.userField.get().getName());
            }
        });
    }
}
