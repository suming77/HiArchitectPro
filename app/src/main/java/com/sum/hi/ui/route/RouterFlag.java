package com.sum.hi.ui.route;

/**
 * @Author: smy
 * @Date: 2022/2/19 14:35
 * @Desc:
 */
public interface RouterFlag {
    int FLAG_LOGIN = 0x01;
    int FLAG_AUTHENTICATION = FLAG_LOGIN << 1;
    int FLAG_VIP = FLAG_AUTHENTICATION << 1;
}
