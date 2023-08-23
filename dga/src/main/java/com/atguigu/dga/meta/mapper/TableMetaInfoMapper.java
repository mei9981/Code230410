package com.atguigu.dga.meta.mapper;

import com.atguigu.dga.meta.bean.PageTableMetaInfo;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 元数据表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-08-21
 */
@Mapper
public interface TableMetaInfoMapper extends BaseMapper<TableMetaInfo> {

    //查询符合条件的数据行数
    Integer queryPageDataNums(@Param("db") String schemaName, @Param("table") String tableName,
                                          @Param("level") String dwLevel);

    //查询指定页面的数据
    List<PageTableMetaInfo> queryPageData(@Param("db") String schemaName, @Param("table") String tableName,
                                          @Param("level") String dwLevel, @Param("size") Integer pageSize,
                                          @Param("from") Integer from);

    /*
        查询table_meta_info，把table_meta_info_extra的查询结果 set到 TableMetaInfo对象的tableMetaInfoExtra属性中。

        方式一： 借助MybatisPlus
                   TableMetaInfo m =  TableMetaInfoMapper.getById(tableId);
                   TableMetaInfoExtra tableMetaInfoExtra =  TableMetaInfoExtraMapper.getOne(库名，表名);
                   m.setTableMetaInfoExtra(tableMetaInfoExtra);
                   效率低。
                    库下有100张表。
                        每一张表需要向Mysql服务器发送2次请求。
                            需要共发送 200次请求。

       方式二:  不借助MybatisPlus，自己编写一个sql
                    使用join，一次性把今天要考评表的所有信息全部拿到
                         库下有100张表。
                                需要共发送 1次请求。
     */
    List<TableMetaInfo> queryTableMetaInfo(@Param("db") String db,@Param("assessDate") String assessDate);
}
