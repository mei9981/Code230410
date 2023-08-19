package com.atguigu.springbootdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Smexy on 2023/8/19
 *
 *  @Controller:
 *          作用:
 *              1.标识当前类是一个控制器(用来处理请求的类)
 *              2.当Spring容器扫描顶了这个注解的类时，会自动在容器中帮我们创建一个单例的对象
 *
 *  @RequestMapping: 映射请求。
 *          声明位置： 类上或方法上。
 *
 *          方法上： 指定当前方法可以处理哪些url
 *
 */
@Controller
public class HelloController
{
    /*
        主机名和端口号是不写的！
     */
    @RequestMapping(value = "/hello")
    public String hello(){
        System.out.println("钱收到了，马上给你办事!");
        return "/suc.html";
    }

    public void hi(){
        System.out.println("钱收到了，马上给你办事!");
    }

}
