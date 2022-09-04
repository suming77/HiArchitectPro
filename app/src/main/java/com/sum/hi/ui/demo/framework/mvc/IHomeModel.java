package com.sum.hi.ui.demo.framework.mvc;

import com.sum.hi.hilibrary.User;
import com.sum.hi.hilibrary.annotation.HiCallback;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 07:53
 * @类描述 ${TODO}
 */
interface IHomeModel {
    public void getUserInfo(HiCallback<User> callback);
}
