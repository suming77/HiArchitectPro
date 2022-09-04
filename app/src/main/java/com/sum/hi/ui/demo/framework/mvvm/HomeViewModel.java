package com.sum.hi.ui.demo.framework.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sum.hi.hilibrary.User;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/08/26 23:31
 * @类描述 ${TODO}
 */
class HomeViewModel extends ViewModel {
    public LiveData<User> getUserInfo() {
        MutableLiveData<User> liveData = new MutableLiveData<>();
        User user = new User("");
        user.setName("呵呵呵");
        liveData.postValue(user);
        return liveData;
    }
}
