package com.atguigu.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    标注了@SpringBootApplication的类，称为程序的主启动类。

    容器会在主启动类的main运行后，扫描这个类的包及子包下的所有类！
 */
@SpringBootApplication
public class SpringbootdemoApplication
{

    public static void main(String[] args) {
        SpringApplication.run(SpringbootdemoApplication.class, args);
    }

}
