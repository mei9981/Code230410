package com.atguigu.mybatis.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Smexy on 2023/8/18
 *  javabean:
 *      有无参构造器
 *      为私有属性提供public权限的getter,setter
 *
 *  lombok: 允许提供注解为类生成一些额外的信息
 *      使用： a) 引入依赖
 *            b) 安装插件
 *
 *    @Getter and @Setter: 为类提供getter,setter
 *    @Data:   @Getter and @Setter + 生成toString()
 *    @AllArgsConstructor: 提供全参构造器。根据属性的顺序进行传参
 *    @NoArgsConstructor: 提供空参构造器
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee
{
    private Integer id;
    private String lastName;
    private String gender;
    private String email;

    public static void main(String[] args) {

        Employee employee = new Employee();
        employee.setGender("male");
        System.out.println(employee);

        Employee employee1 = new Employee(1, "jack", "a", "b");
        System.out.println(employee1);

    }

}
