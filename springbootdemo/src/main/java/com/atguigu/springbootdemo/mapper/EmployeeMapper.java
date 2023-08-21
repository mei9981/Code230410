package com.atguigu.springbootdemo.mapper;

import com.atguigu.springbootdemo.bean.Employee;
import com.atguigu.springbootdemo.bean.Region;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Smexy on 2023/8/18
 *  Mybatis提供了动态代理技术，可以在不提供实现类的基础上返回一个接口的实例！
 *
 *      @Mapper 和 @Service 和 @Component的功能一模一样。都是让容器看到后，给你创建单例对象。
 *          额外作用：
 *                  告诉容器，在创建单例对象时，要使用Mybatis提供的动态代理技术！
 *                  告诉程序员，这是一个Mapper
 */
@Mapper
//@DS("mybatis") //默认这个类的所有方法都使用指定的数据源
public interface EmployeeMapper
{
    //根据id查询员工
    Employee getEmpById(Integer id);

    //增删改 写操作 不需要返回值
    void  deleteEmpById(Integer id);

    void insertEmp(Employee employee);

    @DS("mybatis")
    void updateEmp(Employee employee);

    @DS("mybatis")
    List<Employee> getAll();

    @DS("gmall") //局部优先
    List<Region> getAllRegion();


}
