package com.atguigu.dga.config;

import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Smexy on 2023/8/25
 *      编写一些和元数据操作相关的方法
 */
@Component
public class MetaInfoUtil
{
    /*
        提前把所有待考评表的元数据信息全部拿到
            key: 库名.表名
            value： 元数据信息
     */
    public Map<String, TableMetaInfo> tableMetaInfoMap = new HashMap<>();

    /*
      提前把所有待考评表的Task执行元数据信息全部拿到
          key: 库名.表名
          value： Task执行的元数据信息
   */
    public Map<String, TDsTaskInstance> taskInstancesMap = new HashMap<>();
}
