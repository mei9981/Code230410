package com.atguigu.springbootdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @RequestMapping: 映射请求。
 *  *          声明位置： 类上或方法上。
 *  *           类上： 为这个类的所有方法，默认添加一层父路径
 *  *          方法上： 指定当前方法可以处理哪些url
 */
@Controller
@RequestMapping(value = "/a")
public class HiController
{

    @RequestMapping(value = "/b")
    public String hi(){
        System.out.println("哈哈，钱收到了，马上给你办事!");
        return "/suc.html";
    }

}
