package com.atguigu.springbootdemo.service;

import com.atguigu.springbootdemo.bean.Employee;
import com.atguigu.springbootdemo.bean.Region;

import java.util.List;

/**
 * Created by Smexy on 2023/8/19
 *      由于用于在页面需要进行5种业务操作，因此需要提供这5种操作的业务实现方法。
 *          对单个员工进行： crud
 *          查询所有员工
 */
public interface EmployeeService
{
    //根据id查询员工
    Employee getEmpById(Integer id);

    //增删改 写操作 不需要返回值
    void  deleteEmpById(Integer id);

    void insertEmp(Employee employee);

    void updateEmp(Employee employee);

    List<Employee> getAll();

    List<Region> getAllRegion();

    /*String sayHaha();
    String sayXixi();*/
}
