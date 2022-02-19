package com.example.hirouter.model;

import java.util.List;

/**
 * @Author: smy
 * @Date: 2022/2/16 14:46
 * @Desc:
 */
public class BottomBar {
    public int selectTab;
    public List<Tab> tabs;

    public static class Tab {
        public int size;
        public boolean enable;
        public int index;
        public String pageUrl;
        public String title;
    }
}
