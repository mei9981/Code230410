package com.atguigu.controller;

import com.atguigu.bean.Employee;
import com.atguigu.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

 */
@RestController
public class EmpController
{

    @Autowired
    private EmployeeService service;
    //处理单个员工  /emp?op=select&id=&lastname=&gender=&email=
    @RequestMapping("/emp")
    public Object handle1(String op,String lastname,String gender,String email,Integer id){

        //把参数封装为数据模型
        Employee employee = new Employee(id, lastname, gender, email,"20","aa");

        //调用业务模型，执行业务功能
        switch (op){
            case "select":  if (id == null){
                return "id非法!";
            }else {
                Employee e = service.getById(id);
                return e == null ? "员工不存在!" : e;
            }
            case "delete":  if (id == null){
                return "id非法!";
            }else {
                service.removeById(id);
                return "ok";
            }
            case "update":  if (id == null){
                return "id非法!";
            }else {
                service.updateById(employee);
                return "ok";
            }
            case "insert":
                service.save(employee);
                return "ok";
            default: return "ok";
        }
    }

    //处理多个员工
    @RequestMapping("/getAllEmp")
    public Object handle2(){
        return service.list();
    }


}
