package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.Emp;
import com.atguigu.mybatis.beans.Employee;

import java.util.List;

/**
 * Created by Smexy on 2023/8/18
 *
 *  Mapper(Mybatis) 等价于  Dao(其他框架)
 *
 *      mapper: 编写了sql的xml文件
 *      Mapper: Dao层接口
 *
 *   -----------
 *      使用Mybatis，可以不为接口提供实现类，就可以拥有接口的实例。
 *          通过Mybatis提供的动态代理技术。
 *
 */
public interface EmployeeMapper
{
    //根据id查询员工
    Employee getEmpById(Integer id);

    //增删改 写操作 不需要返回值
    void  deleteEmpById(Integer id);

    void insertEmp(Employee employee);

    void updateEmp(Employee employee);

    List<Employee> getAll();

    //演示resultMap
    List<Emp> getAllEmp();

}
