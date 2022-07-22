package com.sum.hi.ui.pattern.decorator;

import com.sum.hi.ui.demo.B;

/**
 * @author smy
 * @date 2022/7/19
 * @desc
 */
public class DecoratorTest {
    public static void main(String[] args) {
        //创建被装饰者
        Panda panda = new Panda();

        //熊猫被装饰了竹子，可以吃竹子了
        BambooFood bambooFood = new BambooFood(panda);
        //可以吃竹子的熊猫，被装饰了胡萝卜，可以吃胡萝卜
        CarrotFood carrotFood = new CarrotFood(bambooFood);
        carrotFood.eat();

        //打印数据：
        //什么都没有，不知道吃什么
        //可以吃竹子
        //可以吃胡萝卜
    }
}
