package com.atguigu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//默认情况下，以Bean的小写名字作为要操作的表名
@TableName("emp")
public class Employee
{
    /*
        @TableId 代表这一个属性是表的主键列
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String lastName;
    private String gender;
    private String email;

    //告诉MybatisPlus，这两个属性和表没有任何关系，在CRUD时，无需考虑
    @TableField(exist = false)
    private String age;
    @TableField(exist = false)
    private String address;

}
