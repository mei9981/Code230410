package com.atguigu.dga;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DgaApplicationTests
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

}
