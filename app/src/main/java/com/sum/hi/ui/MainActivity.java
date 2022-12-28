package com.sum.hi.ui;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sum.hi.common.component.HiBaseActivity;
import com.sum.hi.ui.aspectj.MethodTrace;
import com.sum.hi.ui.demo.handler.HotFix;
import com.sum.hi.ui.demo.handler.HotFixTest;
import com.sum.hi.ui.logic.MainActivityLogic;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import cn.samsclub.app.product.views.TaskStartUp;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/12/03 23:40
 * @类描述 ${TODO}
 */
public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {

    private MainActivityLogic activityLogic;
    private String CATEGORY_FRAGMENT = "CategoryFragment";
    private List<Fragment> mList;

    @MethodTrace
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hiui);  
        setContentView(R.layout.activity_main);
        activityLogic = new MainActivityLogic(this, savedInstanceState);
//        TaskStartUp.start();
        HiStatusBar.INSTANCE.setStatusBar(this, true, Color.WHITE, false);

/*        findViewById(R.id.tv_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainDemoActivity.class));
            }
        });*/
/*
        ActivityManager.Companion.getInstance().addFrontBackCallback(new FrontBackCallback() {

            @Override
            public void onChange(boolean font) {

                Toast.makeText(MainActivity.this, "前台或者后台：fornt == " + font, Toast.LENGTH_SHORT).show();
            }
        });*/
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

        RecyclerView recyclerView = new RecyclerView(this);
        //        recyclerView.setLayoutManager();

        //        ThreadDemoActivity.testLooperThread();
        findViewById(R.id.tv_test).setOnClickListener(v -> {
            test();
        });
        findViewById(R.id.tv_fix).setOnClickListener(v -> {
            fixBug();
        });
    }

    @MethodTrace
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void test() {

        Toast.makeText(this, new HotFixTest().test(), Toast.LENGTH_LONG).show();
    }

    public void fixBug() {

        String readExternalStorage = permission.READ_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(this, readExternalStorage) == PackageManager.PERMISSION_GRANTED) {
            fix();
        } else {
            String[] strings = new String[]{readExternalStorage};
            ActivityCompat.requestPermissions(this, strings, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fix();
        }
    }

    private void fix() {

        try {
            HotFix.fix(this, new File(Environment.getExternalStorageDirectory(), "patch.dex"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public class MyFragmentAdapter extends FragmentStateAdapter {

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
//        Log.e("smy", "onSaveInstanceState " + outState.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        Log.e("smy", "onRestoreInstanceState " + savedInstanceState.toString());
    }

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {

        return new Object();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            //Activity来分发给每个Fragment, 每个Fragment接收到时都需要判断是不是自己的
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 监听音量下键显示debugtools
     *
     * @param keyCode
     * @param event
     *
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (BuildConfig.DEBUG) {
                //通过反射的形式，不能用引用的方式，因为在正式环境中找不到这个类的
                try {
                    Class<?> aClass = Class.forName("com.sum.hi_debugtool.DebugToolDialogFragment");
                    DialogFragment target = (DialogFragment) aClass.getConstructor().newInstance();
                    target.show(getSupportFragmentManager(), "debug_tools");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
