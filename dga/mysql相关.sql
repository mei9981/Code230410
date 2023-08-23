-- 返回的列名，不能有重复的，因此把重复冲突的列名起别名
select
    t2.*,
    t1.id extraId,
    t1.table_name extraTn,
    t1.schema_name extraSn,
    tec_owner_user_name,
    busi_owner_user_name,
    lifecycle_type,
    lifecycle_days,
    security_level,
    dw_level,
    t1.create_time extraCt,
    t1.update_time extraUt

from (select
            *
      from table_meta_info_extra
      where schema_name = 'gmall'  ) t1
join
     (select
            *
      from table_meta_info
      where schema_name = 'gmall' and assess_date = '2023-08-22') t2
on t1.table_name = t2.table_name and t1.schema_name = t2.schema_name;





CREATE DATABASE `dga`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci ;

create table  table_meta_info
(
    id                      bigint auto_increment comment '表id' primary key,
    table_name              varchar(200)  null comment '表名',
    schema_name             varchar(200)  null comment '库名',
    col_name_json           varchar(2000) null comment '字段名json ( 来源:hive)',
    partition_col_name_json varchar(4000) null comment '分区字段名json( 来源:hive)',
    table_fs_owner          varchar(200)  null comment 'hdfs所属人 ( 来源:hive)',
    table_parameters_json   varchar(2000) null comment '参数信息 ( 来源:hive)',
    table_comment           varchar(200)  null comment '表备注 ( 来源:hive)',
    table_fs_path           varchar(200)  null comment 'hdfs路径 ( 来源:hive)',
    table_input_format      varchar(200)  null comment '输入格式( 来源:hive)',
    table_output_format     varchar(200)  null comment '输出格式 ( 来源:hive)',
    table_row_format_serde  varchar(200)  null comment '行格式 ( 来源:hive)',
    table_create_time       varchar(200)  null comment '表创建时间 ( 来源:hive)',
    table_type              varchar(200)  null comment '表类型 ( 来源:hive)',
    table_bucket_cols_json  varchar(200)  null comment '分桶列 ( 来源:hive)',
    table_bucket_num        bigint        null comment '分桶个数 ( 来源:hive)',
    table_sort_cols_json    varchar(200)  null comment '排序列 ( 来源:hive)',
    table_size              bigint        null comment '数据量大小 ( 来源:hdfs)',
    table_total_size        bigint        null comment '所有副本数据总量大小  ( 来源:hdfs)',
    table_last_modify_time  datetime      null comment '最后修改时间   ( 来源:hdfs)',
    table_last_access_time  datetime      null comment '最后访问时间   ( 来源:hdfs)',
    fs_capcity_size         bigint        null comment '当前文件系统容量   ( 来源:hdfs)',
    fs_used_size            bigint        null comment '当前文件系统使用量   ( 来源:hdfs)',
    fs_remain_size          bigint        null comment '当前文件系统剩余量   ( 来源:hdfs)',
    assess_date             varchar(10)   null comment '考评日期 ',
    create_time             datetime      null comment '创建时间 (自动生成)',
    update_time             datetime      null comment '更新时间  (自动生成)',
    constraint table_meta_info_pk
        unique (table_name, schema_name, assess_date)
)
    comment '元数据表';

create table if not exists table_meta_info_extra
(
    id                   bigint auto_increment comment 'id'
        primary key,
    table_name           varchar(200) null comment '表名',
    schema_name          varchar(200) null comment '库名',
    tec_owner_user_name  varchar(20)  null comment '技术负责人   ',
    busi_owner_user_name varchar(20)  null comment '业务负责人 ',
    lifecycle_type       varchar(20)  null comment '存储周期类型',
    lifecycle_days       bigint       null comment '生命周期(天) ',
    security_level       varchar(20)  null comment '安全级别',
    dw_level             varchar(20)  null comment '数仓所在层级',
    create_time          datetime     null comment '创建时间 (自动生成)',
    update_time          datetime     null comment '更新时间  (自动生成)',
    constraint table_meta_info_extra_pk
        unique (table_name, schema_name)
)
    comment '元数据表附加信息';


create table governance_metric
(
    id                 bigint auto_increment comment 'id' primary key,
    metric_name        varchar(200)  null comment '指标名称',
    metric_code        varchar(200)  null comment '指标编码',
    metric_desc        varchar(2000) null comment '指标描述',
    governance_type    varchar(20)   null comment '治理类型',
    metric_params_json varchar(2000) null comment '指标参数',
    governance_url     varchar(500)  null comment '治理连接',
    is_disabled        varchar(1)    null comment '是否启用'
)
    comment '考评指标参数表';