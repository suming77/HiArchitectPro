package com.sum.hi.hiui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
public class HiTabBottom extends RelativeLayout implements IHiTab<HiTabBottomInfo<?>> {

    private ImageView ivImage;
    private TextView tvIcon;
    private TextView tvName;

    private HiTabBottomInfo<?> tabInfo;

    public HiTabBottom(Context context) {
        this(context, null);
    }

    public HiTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_bottom, this);
        ivImage = findViewById(R.id.iv_image);
        tvIcon = findViewById(R.id.tv_icon);
        tvName = findViewById(R.id.tv_name);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabBottomInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }


    /**
     * @param selected 是否选择
     * @param init     是否初始化
     */
    private void inflateInfo(boolean selected, boolean init) {
        if (tabInfo.tabType == HiTabBottomInfo.TabType.ICON) {
            if (init) {
                ivImage.setVisibility(GONE);
                tvIcon.setVisibility(VISIBLE);

                Typeface fromAsset = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tvIcon.setTypeface(fromAsset);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tvName.setText(tabInfo.name);
                }
            }

            if (selected) {
                tvIcon.setText(TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.defaultIconName : tabInfo.selectedIconName);
                tvIcon.setTextColor(getTextColor(tabInfo.tintColor));
                tvName.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tvIcon.setText(tabInfo.defaultIconName);
                tvIcon.setTextColor(getTextColor(tabInfo.defaultColor));
                tvName.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == HiTabBottomInfo.TabType.BITMAP) {
            if (init) {
                ivImage.setVisibility(VISIBLE);
                tvIcon.setVisibility(GONE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tvName.setText(tabInfo.name);
                }
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
    public void onTabSelectedChange(int index, @Nullable HiTabBottomInfo<?> prevInfo, @NonNull HiTabBottomInfo<?> nextInfo) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return;
        }
        if (prevInfo == tabInfo) {
            inflateInfo(false, false);
        } else {
            inflateInfo(true, false);
        }
    }

    public HiTabBottomInfo<?> getHiTabInfo() {
        return tabInfo;
    }

    public ImageView getIvImage() {
        return ivImage;
    }

    public TextView getTvIcon() {
        return tvIcon;
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
