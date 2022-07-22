package com.sum.hi.ui.pattern.decorator;

/**
 * @author smy
 * @date 2022/7/19
 * @desc
 */
//具体装饰
public class BambooFood extends Food{
    public BambooFood(Animal animal) {
        super(animal);
    }

    @Override
    public void eat() {
        super.eat();
        System.out.println("可以吃竹子");
    }
}
