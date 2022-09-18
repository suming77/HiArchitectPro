package com.sum.hi.ui.demo.headviewpager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.sum.hi.hilibrary.util.HiDisplayUtil;
import com.sum.hi.ui.R;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/18 21:45
 * @类描述 ${TODO}
 */
public class HeaderViewPagerDemo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_view_pager);

        HeaderViewPager headerViewPager = findViewById(R.id.head_view_pager);
        ScrollView scrollView = findViewById(R.id.scrollView);
        RelativeLayout rl_head = findViewById(R.id.rl_head);

        ViewGroup.LayoutParams params = rl_head.getLayoutParams();
        params.height = HiDisplayUtil.getDisplayHeightInPx(this)/2;
        rl_head.setLayoutParams(params);
        headerViewPager.post(new Runnable() {
            @Override
            public void run() {
                headerViewPager.setTopOffset(HiDisplayUtil.dp2px(140));
                headerViewPager.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer() {
                    @Override
                    public View getScrollableView() {
                        return scrollView;
                    }
                });
            }
        });

        headerViewPager.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                Log.i("smy", "currentY==" + currentY + "|maxY:" + maxY);
            }
        });


    }
}
