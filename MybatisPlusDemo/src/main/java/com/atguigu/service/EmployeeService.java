package com.atguigu.service;

import com.atguigu.bean.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Created by Smexy on 2023/8/21
 */
public interface EmployeeService extends IService<Employee>
{
    List<Employee> getEmps();
}
