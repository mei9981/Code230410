package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Smexy on 2023/8/18
 *
        sql语句可以写在xml中(利于维护)，也可以直接写在方法上(方便)。

        不管写在哪里，都要在全局的配置中去注册！
 *
 */
public interface EmployeeMapper2
{
    //根据id查询员工
    @Select(" select * from employee where id = #{ageakljga} ")
    Employee getEmpById(Integer id);

    //增删改 写操作 不需要返回值
    @Delete("delete from employee where id = #{xxxx}")
    void  deleteEmpById(Integer id);

    @Insert("        insert into employee(last_name,gender,email) values(#{lastName},#{gender},#{email})\n")
    void insertEmp(Employee employee);

    @Update(" update employee set last_name = #{lastName} , gender = #{gender} ,email = #{email}\n" +
        "        where id = #{id}")
    void updateEmp(Employee employee);

    @Select(" select * from employee ")
    List<Employee> getAll();

}
