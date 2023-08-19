package com.atguigu.springbootdemo;

import com.atguigu.springbootdemo.bean.Dog;
import com.atguigu.springbootdemo.bean.TuDog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootTest
class SpringbootdemoApplicationTests
{

    /*
         传统做法: Dog dog =  new Dog();
            new Dog(): 反转了，只需要写注解，通知容器做！
            =：  @Autowired

           @Autowired： 到容器中找到一个Dog类型的对象，赋值给当前变量

     */
    @Autowired
    private Dog dog;

    @Autowired
    private LocalDate today;
    @Test
    void contextLoads() {

        System.out.println(dog);
        System.out.println(today);
    }

    @Value("${my.name}")
    private String name;
    //容器对象
    @Autowired
    private ApplicationContext context;
    /*
        手动获取对象
     */
    @Test
    void manualGetBean(){
        //按照类型从容器中取
        TuDog d1 = context.getBean(TuDog.class);
        System.out.println(d1);

        //容器中存在多个类型的对象，需要再指定id
        Dog d2 = context.getBean("dog",Dog.class);
        System.out.println(d2);
        System.out.println(name);
    }

}
