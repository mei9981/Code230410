package com.atguigu.springbootdemo.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/*
    @Component: 作用就是当容器扫描到后，会自动在容器中为这个类创建一个单例对象！
 */
@Component
@Data
public class Dog
{
    private String name = "旺财";
    private Integer age = 5;

    public Dog(){
        System.err.println(name +"被创建了");
    }
}