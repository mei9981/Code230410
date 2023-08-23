package com.atguigu.dga.meta.service.impl;

import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.meta.bean.TableMetaInfoExtra;
import com.atguigu.dga.meta.mapper.TableMetaInfoExtraMapper;
import com.atguigu.dga.meta.service.TableMetaInfoExtraService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 元数据表附加信息 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-22
 */
@Service
public class TableMetaInfoExtraServiceImpl extends ServiceImpl<TableMetaInfoExtraMapper, TableMetaInfoExtra> implements TableMetaInfoExtraService {

    //@Autowired
    private HiveMetaStoreClient client;

    @Override
    public void initExtraMetaInfo(String db) throws Exception {

        List<TableMetaInfoExtra> result = new ArrayList<>();

        String [] tecOwners = {"张三","李强","王红","赵五","陈帅"};
        String [] busiOwners = {"张四","李不强","王绿","赵六","陈丑"};
        //获取库下所有的表
        List<String> tables = client.getAllTables(db);
        //如果这个表的辅助信息已经存在，无需生成
        for (String table : tables) {

            TableMetaInfoExtra infoExtra = getOne(new QueryWrapper<TableMetaInfoExtra>().eq("schema_name", db).eq("table_name", table));

            //表的辅助信息还没有，当前表是一张新表，需要为它生成辅助信息
            if (infoExtra == null){
                infoExtra = new TableMetaInfoExtra();
                infoExtra.setTableName(table);
                infoExtra.setSchemaName(db);
                //关于表的负责人，应该由管理系统录入  意思意思
                infoExtra.setTecOwnerUserName(tecOwners[RandomUtils.nextInt(0,tecOwners.length)]);
                infoExtra.setBusiOwnerUserName(busiOwners[RandomUtils.nextInt(0,busiOwners.length)]);
                infoExtra.setLifecycleType(MetaConstant.LIFECYCLE_TYPE_UNSET);
                infoExtra.setLifecycleDays(-1l);
                infoExtra.setSecurityLevel(MetaConstant.SECURITY_LEVEL_UNSET);
                //根据表的名字去设置分层
                infoExtra.setDwLevel(getLevel(table));
                infoExtra.setCreateTime(new Timestamp(System.currentTimeMillis()));
                result.add(infoExtra);
            }
        }

        //写入数据库
        saveBatch(result);
    }

    private String getLevel(String name){

        //转换为大写
        String upperCaseTable = name.toUpperCase();
        //截取前5位，看是否包含特定的字符串
        String prefix = upperCaseTable.substring(0, 5);
        //判断
        if (prefix.contains(MetaConstant.DW_LEVEL_ODS)){
            return MetaConstant.DW_LEVEL_ODS;
        }else if (prefix.contains(MetaConstant.DW_LEVEL_DIM)){
            return MetaConstant.DW_LEVEL_DIM;
        }else if (prefix.contains(MetaConstant.DW_LEVEL_DWD)){
            return MetaConstant.DW_LEVEL_DWD;
        }else if (prefix.contains(MetaConstant.DW_LEVEL_DWS)){
            return MetaConstant.DW_LEVEL_DWS;
        }else if (prefix.contains(MetaConstant.DW_LEVEL_ADS)){
            return MetaConstant.DW_LEVEL_ADS;
        }else if (prefix.contains(MetaConstant.DW_LEVEL_DM)){
            return MetaConstant.DW_LEVEL_DM;
        }else {
            return MetaConstant.DW_LEVEL_OTHER;
        }

    }
}
