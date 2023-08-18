package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.beans.Employee;
import com.atguigu.mybatis.beans.MyBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * Created by Smexy on 2023/8/18
 *
 *  如何向sql中传参。
 *      如果sql中需要多个占位符，这些参数都是从方法的参数列表中传入，那么方法的参数列表如何声明？
 *       参数非常多！
 *          方式一： 可以使用Map集合封装
 *                      此时使用 #{key} 来获取对应的value
 *
 *          方式二:  可以使用Bean封装
 *                      此时使用 #{属性名} 来获取对应的value
 *
 *       参数不是很多，可以直接铺开！
 *          方式三:  直接铺开，使用常见的字面量声明！
 *                      此时使用 #{ arg0,...,argn } 代表第N个参数
 *                      此时使用 #{ param1,...,param3 } 代表第N个参数
 *
 *    ---------------------------
 *      总结： 如何从方法的参数列表中，使用#{xxx}来获取传入的值。
 *          方法的参数列表中，只有1个字面量参数:  xxx 随便写
 *          方法的参数列表中，只有1个Bean:  xxx，写Bean的属性
 *          方法的参数列表中，只有1个Map:  xxx，写key
 *          方法的参数列表中，有N个字面量参数:
 *                  建议每个参数都使用@Param注解，明确指定参数名
 *                  xxx，写@Param注解定义的名字
 *
 */
public interface ParamPassMapper
{
    @Select("select * from employee where id= #{a} and last_name= #{b} and gender = #{c}")
    Employee query1(Map<String,Object> param);

    @Select("select * from employee where id= #{aa} and last_name= #{cc} and gender = #{bb}")
    Employee query2(MyBean bean);

    //@Select("select * from employee where id= #{arg0} and last_name= #{arg1} and gender = #{arg2}")
    //@Select("select * from employee where id= #{param1} and last_name= #{param2} and gender = #{param3}")
    @Select("select * from employee where id= #{arg0} and last_name= #{param2} and gender = #{param3}")
    Employee query3(Integer a,String name,String g);

    @Select("select * from employee where id= #{A} and last_name= #{B} and gender = #{C}")
    Employee query4(@Param("A") Integer a, @Param("B")String name, @Param("C")String g);
}
