package com.sum.hi.ui;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.sum.hi.common.component.HiBaseActivity;
import com.sum.hi.ui.demo.MainDemoActivity;
import com.sum.hi.ui.fragment.CategoryFragment;
import com.sum.hi.ui.fragment.FavoriteFragment;
import com.sum.hi.ui.fragment.HomeFragment;
import com.sum.hi.ui.fragment.ProfileFragment;
import com.sum.hi.ui.fragment.RecommendFragment;
import com.sum.hi.ui.logic.MainActivityLogic;
import com.sum.hi.ui.tab.ActivityManager;
import com.sum.hi.ui.tab.ActivityManager.FrontBackCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/03 23:40
 * @类描述 ${TODO}
 */
public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {

    private MainActivityLogic activityLogic;
    private String CATEGORY_FRAGMENT = "CategoryFragment";
    private List<Fragment> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityLogic = new MainActivityLogic(this, savedInstanceState);
/*        findViewById(R.id.tv_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainDemoActivity.class));
            }
        });*/

        ActivityManager.Companion.getInstance().addFrontBackCallback(new FrontBackCallback() {

            @Override
            public void onChange(boolean font) {

                Toast.makeText(MainActivity.this, "前台或者后台：fornt == " + font, Toast.LENGTH_SHORT).show();
            }
        });
/*
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CATEGORY_FRAGMENT);
        if (fragment == null) {
            CategoryFragment categoryFragment = new CategoryFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.constraint, categoryFragment, CATEGORY_FRAGMENT)
                    .commit();
        }*/

//        HomeFragment homeFragment = new HomeFragment();
//        FavoriteFragment favoriteFragment = new FavoriteFragment();
//        ProfileFragment profileFragment = new ProfileFragment();
//        RecommendFragment recommendFragment = new RecommendFragment();
//
//        mList = new ArrayList();
//        mList.add(homeFragment);
//        mList.add(favoriteFragment);
//        mList.add(profileFragment);
//        mList.add(recommendFragment);
//
//        ViewPager2 viewPager = findViewById(R.id.viewPager);
//        viewPager.setAdapter(new MyFragmentAdapter(this));
//        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        RecyclerView recyclerView=new RecyclerView(this);
//        recyclerView.setLayoutManager();
    }

    public class MyFragmentAdapter extends FragmentStateAdapter{

        public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {

            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            return mList.get(position);
        }

        @Override
        public int getItemCount() {

            return mList.size();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
//        activityLogic.onSaveInstanceState(outState);
        Log.e("smy", "onSaveInstanceState " + outState.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        Log.e("smy", "onRestoreInstanceState " + savedInstanceState.toString());
    }
}
