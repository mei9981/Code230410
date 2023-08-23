package com.atguigu.dga;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.atguigu.dga.meta.mapper.TableMetaInfoMapper;
import com.atguigu.dga.meta.service.TableMetaInfoService;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MetaTests
{
    @Autowired
    private HiveMetaStoreClient client;
    @Test
    void testHiveClient() throws TException {

        //获取所有的库
        List<String> dbs = client.getAllDatabases();
        System.out.println(dbs);

        //获取某个库下所有的表
        List<String> tables = client.getAllTables("gmall");
        System.out.println(tables);

        //获取某个表的元数据信息
        Table table = client.getTable("gmall", "ods_log_inc");
        System.out.println(table);

    }

    @Test
    void testColJson() throws TException {

        //获取某个表的元数据信息
        Table table = client.getTable("gmall", "ods_log_inc");
        //System.out.println(table);

        StorageDescriptor sd = table.getSd();

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter("name", "type", "comment");
        System.out.println(JSON.toJSONString(sd.getCols(),filter));

    }

    @Autowired
    private TableMetaInfoService service;

    @Test
    void testExtractHiveMetaInfo() throws Exception {
        service.initTableMetaInfo("gmall","2023-08-22");
    }

    @Autowired
    private TableMetaInfoMapper tableMetaInfoMapper;

    @Test
    void testPage(){
        /*List<PageTableMetaInfo> data = tableMetaInfoMapper.queryPageData("", "", "", 10, 1);
        System.out.println(data);*/

        Integer nums = tableMetaInfoMapper.queryPageDataNums("", "", "");
        System.out.println(nums);
    }

}
