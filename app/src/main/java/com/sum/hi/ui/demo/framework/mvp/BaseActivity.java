package com.sum.hi.ui.demo.framework.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 11:47
 * @类描述 ${TODO}
 */
public class BaseActivity<p extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected p mPrensenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //当前类的父类
        Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {//父类是泛型参数类型
            Type[] typeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
            if (typeArguments != null && typeArguments[0] instanceof BasePresenter) {
                //直接构造出实例对象
                try {
                    mPrensenter = (p) typeArguments[0].getClass().newInstance();
                    mPrensenter.attach(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放view
        if (mPrensenter != null) {
            mPrensenter.detach();
        }
    }

    @Override
    public boolean isAlive() {
        return !isDestroyed() && !isFinishing();
    }
}
