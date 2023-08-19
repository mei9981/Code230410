package com.atguigu.mybatis.demos;

import java.util.Comparator;

/**
 * Created by Smexy on 2023/8/19
 *
 *  lamda表达式是java8退出的功能，距离现在已经十几年了。
 *
 *      lamda表达式是使用函数式编程的思想，帮助我们为函数式接口创建对象。
 *
 *      函数式编程的思想：
 *          (函数的方法列表) -> {函数体}
 *
 *       函数式接口：
 *              标注了@FunctionalInterface注解的接口.
 *              和普通的接口的区别在于，只有一个抽象方法。
 *
 *       如何为接口创建对象?
 *           自己编写类实现接口: 外部类 | (静态)内部类
 *           接口的实现只使用一次:  使用匿名内部类 | lamda表达式
 *
 *     -----------------------------------
 *      lamda表达式的格式:
 *          (要实现的抽象方法的参数列表) -> {要实现的抽象方法的函数体}
 *              对于(): 参数的类型是可以省略，由编译器去推导
 *                      参数的个数只有一个，()可以省略
 *
 *              对于{}: 如果方法的实现只有一行，可以省略{},
 *                     如果这一行是return xxx，可以省略return
 */
public class LamdaDemo
{
    public static void main(String[] args) {

        Comparator c1= new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        System.out.println(c1.compare(1, 2));

        //lamda表达式
        Comparator<Integer> c2 = ( o1,  o2) -> o1.compareTo(o2);
        System.out.println(c2.compare(1, 2));

        /*
            lamda表达式的特殊实现
            当前c3对象的compare的逻辑和 Integer类的compare()方法一模一样！
            本着不要重复造轮子的思想，可以直接告诉编译器，我这个compare方法的实现就是
                Integer类的compare()方法的实现，你可以自己去找！
         */
        Comparator<Integer> c3 = (Integer x, Integer y) -> {
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        };
        System.out.println(c3.compare(1, 2));

        //这种情况称为方法引用。 lamda表达式的实现，不用自己写，而是直接引用一个现成的方法！
        Comparator<Integer> c4 =  Integer::compare;
        System.out.println(c4.compare(1, 2));


    }
}
