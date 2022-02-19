package com.example.hirouter.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.DialogFragmentNavigator;
import androidx.navigation.fragment.FragmentNavigator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.hirouter.R;
import com.example.hirouter.model.Destination;
import com.example.hirouter.model.BottomBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: smy
 * @Date: 2022/2/16 11:18
 * @Desc:
 */
public class NavUtils {

    private static HashMap<String, Destination> sDestinationHashMap;

    public static String parseFile(Context context, String fileName) {
        AssetManager assets = context.getAssets();
        try {
            InputStream inputStream = assets.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            if ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            reader.close();
            inputStream.close();

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void builderNavGraph(FragmentActivity activity, NavController controller, FragmentManager childFragmentManager, int containerId) {
        String result = parseFile(activity, "destination.json");

        int c = 0x01;
        int a = c << 1;
        int b = a << 1;
        int d = b << 1;
        if ((a & b) != 0) {

        }
        Log.e("smy", "& 比较：a == " + a + " | b == " + b + " | d ==" + d);
        Log.e("smy", "& 比较：ab == " + ((a & b) != 0) + " | bb == " + ((b & b) != 0));

        sDestinationHashMap = JSON.parseObject(result, new TypeReference<HashMap<String, Destination>>() {
        }.getType());
        if (sDestinationHashMap == null) {
            return;
        }
        Iterator<Destination> iterator = sDestinationHashMap.values().iterator();

        //获取NavigatorProvider
        NavigatorProvider provider = controller.getNavigatorProvider();
        //获取NavGraphNavigator
        NavGraphNavigator graphNavigator = provider.getNavigator(NavGraphNavigator.class);
        //创建NavGraph
        NavGraph graph = new NavGraph(graphNavigator);

        //navigator.createDestination() 里面创建的navigator用的是child的
        //自定义的FragmentNavigator
        HiFragmentNavigator hiFragmentNavigator = new HiFragmentNavigator(activity, childFragmentManager, containerId);
        //注册到里面
        provider.addNavigator(hiFragmentNavigator);

        while (iterator.hasNext()) {
            Destination destination = iterator.next();
            if (destination.destType.equals("Activity")) {
                ActivityNavigator navigator = provider.getNavigator(ActivityNavigator.class);
                ActivityNavigator.Destination node = navigator.createDestination();

                node.setId(destination.id);
                node.setComponentName(new ComponentName(activity.getPackageName(), destination.clazName));

                graph.addDestination(node);
            } else if (destination.destType.equals("fragment")) {
                FragmentNavigator navigator = provider.getNavigator(FragmentNavigator.class);
//                FragmentNavigator.Destination node = navigator.createDestination();
                HiFragmentNavigator.Destination node = hiFragmentNavigator.createDestination();
                node.setId(destination.id);
                node.setClassName(destination.clazName);

                graph.addDestination(node);
            } else if (destination.destType.equals("Dialog")) {
                DialogFragmentNavigator navigator = provider.getNavigator(DialogFragmentNavigator.class);
                DialogFragmentNavigator.Destination node = navigator.createDestination();

                node.setId(destination.id);
                node.setClassName(destination.clazName);

                graph.addDestination(node);
            }

            if (destination.asStarter) {
                graph.setStartDestination(destination.id);
            }
        }
        controller.setGraph(graph);
    }

    public static void builderBottomBar(BottomNavigationView navigationView) {
        String content = parseFile(navigationView.getContext(), "main_tabs_config.json");
        BottomBar bottomBar = JSON.parseObject(content, BottomBar.class);

        List<BottomBar.Tab> tabs = bottomBar.tabs;
        Menu menu = navigationView.getMenu();
        for (BottomBar.Tab tab : tabs) {
            if (!tab.enable) {
                continue;
            }
            Destination destination = sDestinationHashMap.get(tab.pageUrl);
            if (destination != null) {
                MenuItem menuItem = menu.add(0, destination.id, tab.index, tab.title);
                menuItem.setIcon(R.mipmap.ic_launcher);
            }
        }
    }
}
