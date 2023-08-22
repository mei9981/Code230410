package com.atguigu.dga.meta.service;

import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.thrift.TException;

/**
 * <p>
 * 元数据表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-21
 */
public interface TableMetaInfoService extends IService<TableMetaInfo> {

    //手动同步某个库在某一天的元数据
    void initTableMetaInfo(String db,String assessDate) throws Exception;

}
