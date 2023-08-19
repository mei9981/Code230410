package com.atguigu.springbootdemo.service;

import com.atguigu.springbootdemo.bean.Employee;
import com.atguigu.springbootdemo.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Smexy on 2023/8/19
 *  @Service 和 @Component的功能一模一样。
 *      @Service纯粹是为了提醒程序员，这个是一个业务模型！
 */
@Service
public class EmployeeServiceImpl2 implements  EmployeeService
{
    //需要提供一个Dao，可以读写数据库中的employee表
    @Autowired
    private EmployeeMapper mapper;

    @Override
    public Employee getEmpById(Integer id) {
        System.out.println("查询之前，做xxxx事情，已经完成了");
        Employee emp = mapper.getEmpById(id);
        System.out.println("查询之后，做xxxx事情，已经完成了");
        return emp;
    }


    @Override
    public void deleteEmpById(Integer id) {
        System.out.println("删除之前，做xxxx事情，已经完成了");
        mapper.deleteEmpById(id);
        System.out.println("删除之后，做xxxx事情，已经完成了");

    }

    @Override
    public void insertEmp(Employee employee) {
        System.out.println("插入之前，做xxxx事情，已经完成了");
        mapper.insertEmp(employee);
        System.out.println("插入之后，做xxxx事情，已经完成了");

    }

    @Override
    public void updateEmp(Employee employee) {
        System.out.println("更新之前，做xxxx事情，已经完成了");
        mapper.updateEmp(employee);
        System.out.println("更新之后，做xxxx事情，已经完成了");

    }

    @Override
    public List<Employee> getAll() {
        System.out.println("查询之前，做xxxx事情，已经完成了");
        List<Employee> all = mapper.getAll();
        System.out.println("查询之后，做xxxx事情，已经完成了");
        return all;
    }
}
