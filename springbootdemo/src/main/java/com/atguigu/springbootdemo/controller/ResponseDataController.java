package com.atguigu.springbootdemo.controller;

import com.atguigu.springbootdemo.bean.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**

    回复客户端:
        1.页面 html (15年前，过时)
                lamp: linux apache(tomcat) mysql php
        2.页面中的数据 (现在)
            前后端分离开发。 数据交互的格式都是json。
            前端负责页面的设计，页面数据的渲染。前端发送请求，后端只负责处理请求，返回前面页面中需要的数据！

            俗称： 写(数据)接口

        ----------------------
        返回数据给前端，返回的数据分类:
            在方法上，添加注解
            第一类： 字面量
                        springboot直接将数据返回，不做处理！
            第二类:  非字面量
                        springboot使用jackson(处理json)将数据自动转为json，再返回。

        -------------------------
        常见的状态码:

 */
@RestController //@Controller 所有功能 + 为当前类的所有方法自动添加@ResponseBody
public class ResponseDataController
{

    //演示返回字面量
    //@ResponseBody
    @RequestMapping(value = "/returndata")
    public String hi(){
        return "/suc.html";
    }

    //演示返回非字面量
    //@ResponseBody
    @RequestMapping(value = "/returnjson")
    public Object hi1(){
        return  new Employee(1, "jack", "a", "b");
    }

    /*
        默认不限制请求方式。
        @PostMapping限定请求方式,必须是post！如果请求方式不合理，报错405.
        @GetMapping限定请求方式,必须是get！如果请求方式不合理，报错405.
     */
    //@PostMapping(value = "/xixixi")
    @GetMapping(value = "/xixixi")
    public Object hi3(){
        int i = 1 / 0;
        return  "ok";
    }

}
