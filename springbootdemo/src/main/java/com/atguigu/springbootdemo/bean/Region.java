package com.atguigu.springbootdemo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Smexy on 2023/8/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region
{
    private Integer id;
    private String  regionName;
}
