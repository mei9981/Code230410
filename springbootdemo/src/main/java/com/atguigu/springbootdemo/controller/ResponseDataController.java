package com.atguigu.springbootdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**

    回复客户端:
        1.页面 html (15年前，过时)
                lamp: linux apache(tomcat) mysql php
        2.页面中的数据 (现在)
            前后端分离开发。 数据交互的格式都是json。
            前端负责页面的设计，页面数据的渲染。前端发送请求，后端只负责处理请求，返回前面页面中需要的数据！

            俗称： 写(数据)接口
 */
@Controller
@RequestMapping(value = "/a")
public class ResponseDataController
{

    @RequestMapping(value = "/b")
    public String hi(){
        System.out.println("哈哈，钱收到了，马上给你办事!");
        return "/suc.html";
    }

}
