package com.sum.hi.ui.pattern.decorator;

/**
 * @author smy
 * @date 2022/7/19
 * @desc
 */
//装饰者组件
abstract class Food implements Animal {
    //装饰者组件里面要持有抽象组件的成员变量
    Animal animal;

    //需要有抽象组件的构造方法
    public Food(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void eat() {
        animal.eat();
    }
}
