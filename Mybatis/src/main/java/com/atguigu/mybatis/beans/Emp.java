package com.atguigu.mybatis.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    resultType这种封装规则:
        查询的结果和Bean的封装规则:
        使用 Bean的 setXxx(yyy);
            Xxx: 查询的列名
            yyy: 查询的列值
            帮你做下划线和驼峰的映射。
            last_name ===> lastName

        如果属性名和列名差别很大，resultType这种封装规则是无效的！
            需要使用resultMap(自定义封装规则(把列和属性一一对应) )
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp
{
    private Integer id;
    private String name;
    private GenderAndEmail genderAndEmail;
}