package com.sum.hi.ui.demo.headviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
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
public class HeaderViewPagerDemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_view_pager);

        HeaderViewPager headerViewPager = findViewById(R.id.head_view_pager);
        ScrollView scrollView = findViewById(R.id.scrollView);
        RelativeLayout rl_head = findViewById(R.id.rl_head);
        ImageView iv_head = findViewById(R.id.iv_head);
        ImageView iv_goods = findViewById(R.id.iv_goods);
        iv_goods.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HeaderViewPagerDemoActivity.this, CoorActivity.class));
            }
        });
        LayoutParams ivHeadLayoutParams = iv_head.getLayoutParams();
        int headWidth = HiDisplayUtil.getDisplayHeightInPx(this)/4;

        ivHeadLayoutParams.width = headWidth;
        ivHeadLayoutParams.height = headWidth;
        iv_head.setLayoutParams(ivHeadLayoutParams);

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
                // x -》 x/2    x* ？ = x/2         0  -> x   2x -> x
                 //                                           1+1  -> +1
                // 0 -> maxY    1-(currntY/maxY)
                //滑动的比率
                float present = ((float)currentY)/maxY;
                float present2 = 1- ((float)currentY)/maxY;
                iv_head.setScaleX((float) (present2+1)/2);
                iv_head.setScaleY((float) (present2+1)/2);
                Log.i("smy", "present==" + present+", present2:"+present2);

                //滑动定
                if (present<=0){
                    Log.i("smy", "到顶部");

                    //滑到底部
                }else if (present >= 1){
                    Log.i("smy", "到底部");

                    //滑动中
                }else {

                }
            }
        });


    }
}
