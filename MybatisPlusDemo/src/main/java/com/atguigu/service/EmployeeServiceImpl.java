package com.atguigu.service;

import com.atguigu.bean.Employee;
import com.atguigu.mapper.EmployeeMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Smexy on 2023/8/21
 *
 *     interface EmployeeService extends IService<Employee>
 *              既提供了非抽象方法，还提供了抽象方法(必须由子类去实现)
 *
 *     class EmployeeServiceImpl implements EmployeeService
 *              除了从父类继承非抽象方法，需要实现父类中的抽象方法
 *
 *      class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T>
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService
{

}
