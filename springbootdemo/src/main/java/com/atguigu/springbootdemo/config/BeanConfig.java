package com.atguigu.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Created by Smexy on 2023/8/19
 *      顶了这个@Configuration注解的类，在容器启动时，会被扫描到，且会执行类中的方法！
 */
@Configuration
public class BeanConfig
{
    //编写一个方法，通过这个方法构造你想要的对象
    @Bean
    public LocalDate createLocalDate(){
        return LocalDate.parse("2023-08-19");
    }
}
