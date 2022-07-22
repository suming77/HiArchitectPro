package com.sum.hi.ui.pattern.decorator;

/**
 * @author smy
 * @date 2022/7/19
 * @desc
 */
//被装饰者
public class Panda implements Animal{
    @Override
    public void eat() {
        System.out.println("什么都没有，不知道吃什么");
    }
}
