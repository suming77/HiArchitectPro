package com.sum.hi.ui.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sum.hi.hiui.tab.common.IHiTabLayout;
import com.sum.hi.hiui.tab.top.HiTabTopInfo;
import com.sum.hi.hiui.tab.top.HiTabTopLayout;
import com.sum.hi.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/04 22:36
 * @类描述 ${TODO}
 */
public class HiTabTopDemoActivity extends AppCompatActivity {
    String[] tabsStr = new String[]{
            "热门",
            "服装",
            "数码",
            "鞋子",
            "零食",
            "家电",
            "汽车",
            "百货",
            "家居",
            "装修",
            "运动"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_top_demo);
        initTabTop();
    }

    private void initTabTop() {

        HiTabTopLayout tabTopLayout = findViewById(R.id.tab_top_layout);
        List<HiTabTopInfo<?>> infoList = new ArrayList<>();

        int defaultColor = getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = getResources().getColor(R.color.tabBottomTintColor);
        for (String s : tabsStr) {

            HiTabTopInfo<?> topInfo = new HiTabTopInfo<>(s, defaultColor, tintColor);
            infoList.add(topInfo);
        }

        tabTopLayout.inflateInfo(infoList);
        tabTopLayout.addTabSelectedChangeListener(new IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable HiTabTopInfo<?> prevInfo, @NonNull HiTabTopInfo<?> nextInfo) {
                Toast.makeText(HiTabTopDemoActivity.this, nextInfo.name,Toast.LENGTH_SHORT).show();
            }
        });

        tabTopLayout.defaultSelected(infoList.get(0));

    }
}
