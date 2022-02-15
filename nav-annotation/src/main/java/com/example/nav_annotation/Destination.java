package com.example.nav_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @Author: smy
 * @Date: 2022/2/15 12:04
 * @Desc: 注解类
 */
@Target(ElementType.TYPE)//标记只能在一个类上面
public @interface Destination {
    /**
     * 页面在路由中的名称
     *
     * @return
     */
    String pageUrl();

    /**
     * 是否作为路由中的第一个启动项
     *
     * @return
     */
    boolean asStarter() default false;
}
