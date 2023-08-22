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
}
