package com.atguigu.dga.meta.service;

import com.atguigu.dga.meta.bean.PageTableMetaInfo;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.thrift.TException;

import java.util.List;

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

    //查询符合条件的数据行数
    Integer queryPageDataNums( String schemaName,  String tableName,
                               String dwLevel);

    //查询指定页面的数据
    List<PageTableMetaInfo> queryPageData( String schemaName,  String tableName,
                                          String dwLevel,  Integer pageSize,
                                           Integer from);

    //查询今天要参与考评的所有表的元数据+辅助信息
    List<TableMetaInfo> queryTableMetaInfo(String db,String assessDate);

}
