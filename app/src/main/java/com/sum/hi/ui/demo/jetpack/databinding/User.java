package com.sum.hi.ui.demo.jetpack.databinding;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.sum.hi.ui.BR;
/**
 * @author smy
 * @date 2022/8/30 11:40
 * @desc
 */
public class User extends BaseObservable {

    public String name;

    //当使用name字段发生变更后，若想UI自动刷新，我们需要给他写个get方法并且标记Bindable注解
    //最后调用notifyPropertyChanged(_即可
    @Bindable
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
        notifyPropertyChanged(BR.user);
    }
}
