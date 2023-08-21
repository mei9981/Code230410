package com.atguigu.test;

import com.atguigu.bean.Employee;
import com.atguigu.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Created by Smexy on 2023/8/21
 */
@SpringBootTest //加了这个注册才可以使用容器功能
public class MyTest
{
    @Autowired
    private EmployeeService service;

    @Test
    public void test1(){
        System.out.println(service.getEmps());
    }

    /*
        查询id > 5的gender是a的所有员工，按照id进行降序排序，取前2,只查询id,lastName

        QueryWrapper: 查询和删除时，可以传入条件。
        UpdateWrapper： 更新时，可以传入条件

        list(Wrapper<T> queryWrapper):
                Wrapper queryWrapper: 过滤的条件


     */
    @Test
    public void test2(){

        //封装了查询条件的对象
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<Employee>()
            .last(" limit 2 ")
            .select("id", "last_name")
            .gt("id", 5)
            .eq("gender", "a")
            .orderByDesc("id");

        List<Employee> emps = service.list(queryWrapper);

        System.out.println(emps);

    }

    /*
        把gender是a的都更新为b
     */
    @Test
    public void test3(){

        //封装了更新条件的对象
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<Employee>()
            .set("gender", "b")
            .set("email", "b@qq.com")
            .eq("gender", "a");

         service.update(updateWrapper);

    }

    //删除id >12的员工
    @Test
    public void test4(){

        //封装了更新条件的对象
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<Employee>()
            .gt("id", 12);

        service.remove(queryWrapper);

    }

}
