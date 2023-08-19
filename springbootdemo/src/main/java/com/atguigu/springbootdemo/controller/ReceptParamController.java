package com.atguigu.springbootdemo.controller;

import com.atguigu.springbootdemo.bean.MyBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 *
 *  客户端发送参数:
 *          k=v
 *  接收普通参数(非json格式)：
 *      方式一： 可以直接在处理请求的方法的形参位置声明和K一样的参数名，类型兼容即可。
 *
 *      方式二:  参数比较多，可以使用Map 或 Bean来接收。
 *               Bean: 属性名必须和参数名一样
 *               Map:  没有要求,需要添加注解@RequestParam
 *
 *   接收json格式的参数: 只能通过apipost软件来模拟！必须是post请求！
 *
 *      在Bean或Map之前添加注解 @RequestBoby
 *       方式一：  Bean接收
 *       方式二：  Map接收
 *
 */
@Controller
public class ReceptParamController
{

    /*
        localhost:8081/paramtest1?name=jack&age=20&gender=M
     */
    @RequestMapping(value = "/paramtest1")
    public String hi( String name ,Integer age ,String gender){
        System.out.println("哈哈，收到了参数: name："+name + ",age:"+age+",gender:"+gender);
        return "/suc.html";
    }

    @RequestMapping(value = "/paramtest2")
    public String hi2(@RequestParam Map<String,Object> map){
        System.out.println("哈哈，收到了参数: "+map);
        return "/suc.html";
    }

    @RequestMapping(value = "/paramtest3")
    public String hi3(MyBean map){
        System.out.println("哈哈，收到了参数: "+map);
        return "/suc.html";
    }

    //json
    @RequestMapping(value = "/paramtest4")
    public String hi4(@RequestBody MyBean map){
        System.out.println("哈哈，收到了参数: "+map);
        return "/suc.html";
    }

    //json
    @RequestMapping(value = "/paramtest5")
    public String hi5(@RequestBody Map<String,Object> map){
        System.out.println("哈哈，收到了参数: "+map);
        return "/suc.html";
    }

}
