package com.atguigu.dga.config;

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
    public Map<String, TableMetaInfo> tableMetaInfoMap = new HashMap<>();
}
