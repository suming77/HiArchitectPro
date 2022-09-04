package com.sum.hi.ui.demo.framework.mvp;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 10:24
 * @类描述 ${TODO}一般特指Activity、Fragment，可以定义一些通用方法
 */
public interface BaseView {
    boolean isAlive();//判断宿主是否存在，避免NPE
}
