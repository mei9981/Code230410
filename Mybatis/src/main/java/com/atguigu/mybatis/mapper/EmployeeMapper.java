package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.Emp;
import com.atguigu.mybatis.beans.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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


    /*
        演示动态sql
       根据条件查询员工
           如果传入了last_name，使用last_name进行过滤
           如果传入了gender，使用gender进行过滤
           如果传入了email，使用email进行过滤

           where gender = #{g} and last_name = #{name} and email = #{e}
    */
    //@Select(" select * from employee ${} ")
    List<Employee> getEmps2(@Param("name") String last_name, @Param("g")String gender,
                            @Param("e")String email);

}
