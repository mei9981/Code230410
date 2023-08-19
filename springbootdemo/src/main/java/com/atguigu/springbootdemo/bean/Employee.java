package com.atguigu.springbootdemo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
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

    /*public void setLast_name(String a){
        lastName = a;
    }*/

    public static void main(String[] args) {

        Employee employee = new Employee();
        employee.setGender("male");
        employee.setLastName("a");
        System.out.println(employee);

        Employee employee1 = new Employee(1, "jack", "a", "b");
        System.out.println(employee1);

    }

}
