package com.atguigu.dga.meta.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.mapper.TableMetaInfoMapper;
import com.atguigu.dga.meta.service.TableMetaInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 元数据表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-21
 */
@Service
public class TableMetaInfoServiceImpl extends ServiceImpl<TableMetaInfoMapper, TableMetaInfo> implements TableMetaInfoService {

    @Autowired
    private HiveMetaStoreClient client;
    @Value("${hdfs.admin}")
    private String admin;
    @Value("${hdfs.uri}")
    private String hdfsUri;
    @Override
    public void initTableMetaInfo(String db, String assessDate) throws Exception {
        //每次执行同步时，要保证执行结果的幂等性。不要重复地向数据库中同步信息
        //1.在同步之前，先判断指定日期指定库的元数据是否已经存在，如果已经存在，先删除
        remove(new QueryWrapper<TableMetaInfo>().eq("schema_name",db).eq("assess_date",assessDate));
        //2.从hive中获取指定库中所有表的元数据信息
        List<TableMetaInfo> tableMetaInfos = extractMetaInfoFromHive(db, assessDate);
        //3.继续从HDFS获取以上表的其他元数据信息
        extractHDFSMetaInfo(tableMetaInfos);
        //4.存入数据库
        saveBatch(tableMetaInfos);

    }

    private void extractHDFSMetaInfo(List<TableMetaInfo> tableMetaInfos) throws Exception {

        //有一个HDFS的客户端
        FileSystem hdfs = FileSystem.get(new URI(hdfsUri), new Configuration(), admin);
        //返回整个HDFS的状态信息
        FsStatus status = hdfs.getStatus();
        long capacity = status.getCapacity();
        long remaining = status.getRemaining();
        long used = status.getUsed();

        for (TableMetaInfo tableMetaInfo : tableMetaInfos) {

            tableMetaInfo.setFsRemainSize(remaining);
            tableMetaInfo.setFsUsedSize(used);
            tableMetaInfo.setFsCapcitySize(capacity);
            tableMetaInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //表中文件的总大小 ， 表中文件的总副本大小 最近一次修改时间 最近一次访问时间 都需要遍历表目录(递归)，进行累积计算(比较 和 累加)
            //由于要进行累积运算，所以需要设置初始值  建议在bean中为四个属性赋值初始值
            //列出表目录下所有的文件
            FileStatus[] fileStatuses = hdfs.listStatus(new Path(tableMetaInfo.getTableFsPath()));
            statTableSize(fileStatuses,tableMetaInfo,hdfs);
        }
    }

    //对一个目录下的所有内容(FileStatus[] fileStatuses),进行判断，是文件就计算，不是继续重复
    private void statTableSize(FileStatus[] fileStatuses, TableMetaInfo tableMetaInfo, FileSystem hdfs) throws IOException {

        //遍历表目录中的每一个文件
        for (FileStatus fileStatus : fileStatuses) {

            //是文件就开始计算
            if (fileStatus.isFile()){
                //累加
                tableMetaInfo.setTableSize(tableMetaInfo.getTableSize() + fileStatus.getLen());
                tableMetaInfo.setTableTotalSize(tableMetaInfo.getTableTotalSize() + fileStatus.getLen() * fileStatus.getReplication()) ;
                tableMetaInfo.getTableLastModifyTime().setTime(
                    Math.max(tableMetaInfo.getTableLastModifyTime().getTime(), fileStatus.getModificationTime() )
                );
                tableMetaInfo.getTableLastAccessTime().setTime(
                    Math.max(tableMetaInfo.getTableLastAccessTime().getTime(), fileStatus.getAccessTime() )
                );
            }else {
                //是目录，继续列出目录下所有的内容，继续判断每一个内容是不是文件，是文件就及计算，不是文件 继续重复这个步骤
                FileStatus[] subFileStatus = hdfs.listStatus(fileStatus.getPath());
                statTableSize(subFileStatus,tableMetaInfo,hdfs);
            }

        }

    }

    private List<TableMetaInfo> extractMetaInfoFromHive(String db, String assessDate) throws TException {

        //获取库下的所有表
        List<String> tables = client.getAllTables(db);
        //准备返回值
        List<TableMetaInfo> result =new ArrayList<>(tables.size());

        //遍历每一张表，获取元数据信息
        for (String table : tables) {
            Table tableMeta = client.getTable(db, table);
            //抽取单张表的元数据信息
            TableMetaInfo tableMetaInfo = extractSingleTableMeta(tableMeta);
            tableMetaInfo.setAssessDate(assessDate);
            result.add(tableMetaInfo);
        }
        return result;

    }

    private TableMetaInfo extractSingleTableMeta(Table tableMeta) {

        TableMetaInfo tableMetaInfo = new TableMetaInfo();
        //抽取相关属性，且赋值
        tableMetaInfo.setTableName(tableMeta.getTableName());
        tableMetaInfo.setSchemaName(tableMeta.getDbName());
        //在元数据信息中，很多信息都在sd属性中
        StorageDescriptor sd = tableMeta.getSd();
        //数据库中，所有列名为xxx_json都要求把数据封装为json格式
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter("name", "type", "comment");
        tableMetaInfo.setColNameJson(JSON.toJSONString(sd.getCols(),filter));
        tableMetaInfo.setPartitionColNameJson(JSON.toJSONString(tableMeta.getPartitionKeys(),filter));
        tableMetaInfo.setTableFsOwner(tableMeta.getOwner());
        tableMetaInfo.setTableParametersJson(JSON.toJSONString(tableMeta.getParameters()));
        tableMetaInfo.setTableComment(tableMeta.getParameters().get("comment"));
        tableMetaInfo.setTableFsPath(sd.getLocation());
        tableMetaInfo.setTableInputFormat(sd.getInputFormat());
        tableMetaInfo.setTableOutputFormat(sd.getOutputFormat());
        tableMetaInfo.setTableRowFormatSerde(sd.getSerdeInfo().getSerializationLib());
        //注意修改表的列类型
        tableMetaInfo.setTableCreateTime(new Timestamp(tableMeta.getCreateTime() * 1000l));
        tableMetaInfo.setTableType(tableMeta.getTableType());
        tableMetaInfo.setTableBucketColsJson(JSON.toJSONString(sd.getBucketCols(),filter));
        tableMetaInfo.setTableBucketNum(sd.getNumBuckets()+0l);
        tableMetaInfo.setTableSortColsJson(JSON.toJSONString(sd.getSortCols(),filter));

        return tableMetaInfo;
    }
}
