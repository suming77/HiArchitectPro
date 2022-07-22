package com.sum.hi.ui.pattern.decorator;

/**
 * @author smy
 * @date 2022/7/19
 * @desc
 */
//具体装饰
public class CarrotFood extends Food{

    public CarrotFood(Animal animal) {
        super(animal);
    }

    @Override
    public void eat() {
        super.eat();
        System.out.println("可以吃胡萝卜");
    }
}
