package com.atguigu.dga.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Smexy on 2023/8/21
 */
@RestController
public class DgaController
{
    /*
        获取路径上的参数
        http://dga.gmall.com/tableMetaInfo/init-tables/a/Invalid%20date
     */
    @RequestMapping("/tableMetaInfo/init-tables/{db}/{assessDate}")
    public Object handle(@PathVariable("db") String db, @PathVariable("assessDate")String assessDate){
        return "ok";
    }
}
