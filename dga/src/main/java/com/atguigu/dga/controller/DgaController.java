package com.atguigu.dga.controller;

import com.atguigu.dga.meta.service.TableMetaInfoExtraService;
import com.atguigu.dga.meta.service.TableMetaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Smexy on 2023/8/21
 */
@RestController
@RequestMapping("/tableMetaInfo")
public class DgaController
{
    @Autowired
    private TableMetaInfoService metaInfoService;

    @Autowired
    private TableMetaInfoExtraService extraMetaService;
    /*
        手动同步元数据
        获取路径上的参数
        http://dga.gmall.com/tableMetaInfo/init-tables/a/Invalid%20date
     */
    @PostMapping("/init-tables/{db}/{assessDate}")
    public Object handle(@PathVariable("db") String db, @PathVariable("assessDate")String assessDate) throws Exception {
        //同步hive和hdfs的元数据
        metaInfoService.initTableMetaInfo(db,assessDate);
        //生成辅助信息
        extraMetaService.initExtraMetaInfo(db);
        return "success";
    }

    /*
        分页查询  /tableMetaInfo/table-list
            schemaName=a&tableName=b&dwLevel=ODS&pageSize=20&pageNo=1
            pageSize: 每页呈现的数据条数
            pageNo:  页码

            数据的索引是从0开始。

            举例:    pageSize=10
                   客户希望查看pageNo=1， 从第0条数据开始返回，返回10条
                   客户希望查看pageNo=2， 从第10条数据开始返回，返回10条

                   返回数据的起始索引 =  (pageNo - 1) * pageSize

             返回数据格式的封装：
                返回的格式: {}   ,用Map 或 JSONObject封装。
                返回的格式: []   ,用List 或 JSONArray封装。

     */
    @PostMapping("/table-list")
    public Object handle1(String schemaName,String tableName,String dwLevel,Integer pageSize,Integer pageNo ) throws Exception {

        //计算当前应该返回给用户的数据的起始索引
        int from =  (pageNo - 1) * pageSize;

        return "success";
    }
}
