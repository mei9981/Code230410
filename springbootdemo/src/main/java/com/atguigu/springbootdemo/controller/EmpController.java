package com.atguigu.springbootdemo.controller;

import com.atguigu.springbootdemo.bean.Employee;
import com.atguigu.springbootdemo.service.EmployeeService;
import com.atguigu.springbootdemo.service.EmployeeServiceImpl;
import com.atguigu.springbootdemo.service.EmployeeServiceImpl2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Smexy on 2023/8/19
 *  一个url，对应一个处理方法！
 *
 *  所有的开发都是面向接口开发！
 *      不使用接口类型声明变量，会造成维护成本高！
 */
@RestController
public class EmpController
{
    //  service = new EmployeeServiceImpl();
    @Autowired
    private EmployeeService service;
    //处理单个员工  /emp?op=select&id=&lastname=&gender=&email=
    @RequestMapping("/emp")
    public Object handle1(String op,String lastname,String gender,String email,Integer id){

        //把参数封装为数据模型
        Employee employee = new Employee(id, lastname, gender, email);

        //调用业务模型，执行业务功能
        switch (op){
            case "select":  if (id == null){
                return "id非法!";
            }else {
                Employee e = service.getEmpById(id);
                return e == null ? "员工不存在!" : e;
            }
            case "delete":  if (id == null){
                return "id非法!";
            }else {
                service.deleteEmpById(id);
                return "ok";
            }
            case "update":  if (id == null){
                return "id非法!";
            }else {
                service.updateEmp(employee);
                return "ok";
            }
            case "insert":
                service.insertEmp(employee);
                return "ok";
            default: return "ok";
        }
    }

    //处理多个员工
    @RequestMapping("/getAllEmp")
    public Object handle2(){
        return service.getAll();
    }
}
