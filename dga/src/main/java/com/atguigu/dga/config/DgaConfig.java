package com.atguigu.dga.config;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Smexy on 2023/8/21
 */
//@Configuration
public class DgaConfig
{
    @Value("${hive.metastore.uris}")
    private String uri;

    @Value("${hive.metastore.uri.selection}")
    private String uriSelection;
    @Bean
    public HiveMetaStoreClient createHiveMetaStoreClient()  {

        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("hive.metastore.uris",uri);
        conf.set("hive.metastore.uri.selection",uriSelection);
        HiveMetaStoreClient client = null;
        try {
            client = new HiveMetaStoreClient(conf);
        } catch (MetaException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
}
