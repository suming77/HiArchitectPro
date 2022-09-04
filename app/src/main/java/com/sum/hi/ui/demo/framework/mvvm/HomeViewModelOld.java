package com.sum.hi.ui.demo.framework.mvvm;

import androidx.databinding.ObservableField;

import com.sum.hi.hilibrary.User;


/**
 * @创建者 mingyan.su
 * @创建时间 2022/08/25 23:41
 * @类描述 ${TODO}
 */
public class HomeViewModelOld {
    public ObservableField<User> userField = new ObservableField<>();

    /**
     * 并不会通过接口的形式回传数据
     */
    public void queryUserInfo() {
        User user = new User("");
        user.setName("你好");
        //自动通知与之关联的观察者
        userField.set(user);
    }
}
