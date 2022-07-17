package com.sum.hi.hiui.banner.core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sum.hi.hilibrary.util.HiDisplayUtil;
import com.sum.hi.hiui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/01/02 11:50
 * @类描述 ${TODO}Banner 圆形指示器
 */
public class HiCircleIndicator extends FrameLayout implements HiIndicator<FrameLayout> {
    private static final int VWC = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * 正常状态下的指示点
     */
    private @DrawableRes
    int mPointNormal = R.drawable.shape_point_normal;

    /**
     * 选中状态下的指示点
     */
    private @DrawableRes
    int mPointSelected = R.drawable.shape_point_select;

    /**
     * 指示点左右内间距
     */
    private int mPointLeftRightPadding = 0;

    /**
     * 指示点上下内间距
     */
    private int mPointTopBottomPadding = 0;

    public HiCircleIndicator(@NonNull Context context) {
        this(context, null);
    }

    public HiCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPointLeftRightPadding = HiDisplayUtil.dp2px(5, getContext().getResources());
        mPointTopBottomPadding = HiDisplayUtil.dp2px(15, getContext().getResources());
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count <= 0) {
            return;
        }

        LinearLayout groupView = new LinearLayout(getContext());
        groupView.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(VWC, VWC);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(mPointLeftRightPadding, mPointTopBottomPadding, mPointLeftRightPadding, mPointTopBottomPadding);

        for (int i = 0; i < count; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setImageResource(mPointSelected);
            } else {
                imageView.setImageResource(mPointNormal);
            }
            groupView.addView(imageView);
        }
        LayoutParams groupViewParams = new LayoutParams(VWC, VWC);
        groupViewParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        addView(groupView, groupViewParams);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
//                Log.e("smy", "is == "+ (i == current));
            if (i == current) {
                imageView.setImageResource(mPointSelected);
            } else {
                imageView.setImageResource(mPointNormal);
            }
            imageView.requestLayout();
        }
    }
}
