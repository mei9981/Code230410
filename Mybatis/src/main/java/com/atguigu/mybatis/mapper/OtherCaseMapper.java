package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Smexy on 2023/8/18
 *
 *  占位符的格式:
 *      #{xxx}： xxx可以随便写。
 *          一般用在参数位置，自动根据参数类型，添加对应的符号，例如''。
 *          防止sql注入
 *      还可以使用
 *
 *      ${xxxx}: xxxx不能随便写，使用@Param自定义参数
 *          直接拼接，不会做类型的处理！
 *          一般用于拼接sql。
 */
public interface OtherCaseMapper
{
    //查询Tom
    @Select(" select * from employee where last_name = '${s}' ")
    Employee getEmpById(@Param("s") String s);

    //占位的是表名，不能使用#{}，只能使用#{}
    @Select(" select * from ${s} where last_name = 'Tom' ")
    Employee getEmpById2(@Param("s") String s);

    //参数位置使用${}，会有sql注入的风险
    //查询male的员工
    @Select(" select * from employee where gender = ${s} ")
    List<Employee> getEmps(@Param("s") String s);



}
