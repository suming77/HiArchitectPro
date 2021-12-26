package com.sum.hi.hiui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sum.hi.hiui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/08 00:24
 * @类描述 ${TODO}
 */
public class HiTextOverView extends HiOverView {
    private TextView mText;
    private View mRotateView;

    public HiTextOverView(@NonNull Context context) {
        super(context);
    }

    public HiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_refresh_overview, this, true);
        mText = findViewById(R.id.text);
        mRotateView = findViewById(R.id.iv_rotateview);
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {
            mText.setText("下拉刷新");
    }

    @Override
    public void onOver() {
        mText.setText("松开刷新");
    }

    @Override
    public void onRefresh() {
        mText.setText("正在刷新…");
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        animation.setInterpolator(new LinearInterpolator());
        mRotateView.startAnimation(animation);
    }

    @Override
    public void onFinish() {
        mRotateView.clearAnimation();
    }
}
