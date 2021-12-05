package com.sum.hi.hiui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sum.hi.hiui.R;
import com.sum.hi.hiui.tab.common.IHiTab;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/29 00:02
 * @类描述 ${TODO}
 */
public class HiTabTop extends RelativeLayout implements IHiTab<HiTabTopInfo<?>> {

    private ImageView ivImage;
    private TextView tvName;
    private HiTabTopInfo<?> tabInfo;

    private View indicator;

    public HiTabTop(Context context) {
        this(context, null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top, this);
        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }


    /**
     * @param selected 是否选择
     * @param init     是否初始化
     */
    private void inflateInfo(boolean selected, boolean init) {
        if (tabInfo.tabType == HiTabTopInfo.TabType.TEXT) {
            if (init) {
                ivImage.setVisibility(GONE);
                tvName.setVisibility(VISIBLE);

                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tvName.setText(tabInfo.name);
                }
            }

            if (selected) {
                indicator.setVisibility(VISIBLE);
                tvName.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                indicator.setVisibility(GONE);
                tvName.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == HiTabTopInfo.TabType.BITMAP) {
            if (init) {
                ivImage.setVisibility(VISIBLE);
                tvName.setVisibility(GONE);
            }
            if (selected) {
                ivImage.setImageBitmap(tabInfo.selectedBitmap);
            } else {
                ivImage.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    /**
     * 改变某个tab的高度
     *
     * @param height
     */
    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTvName().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable HiTabTopInfo<?> prevInfo, @NonNull HiTabTopInfo<?> nextInfo) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return;
        }
        if (prevInfo == tabInfo) {
            inflateInfo(false, false);
        } else {
            inflateInfo(true, false);
        }
    }

    public HiTabTopInfo<?> getHiTabInfo() {
        return tabInfo;
    }

    public ImageView getIvImage() {
        return ivImage;
    }

    public TextView getTvName() {
        return tvName;
    }

    @ColorInt
    public int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }
}
