package com.atguigu.mapper;

import com.atguigu.bean.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Smexy on 2023/8/21
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>
{
    List<Employee> getEmps();
}
