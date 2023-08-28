select
    null id,
    '2023-05-26' assess_date,
    avg(score_spec_avg) score_spec,
    avg(score_storage_avg) score_storage,
    avg(score_calc_avg) score_calc,
    avg(score_quality_avg) score_quality,
    avg(score_security_avg) score_security,
    avg(score_on_type_weight) score,
    count(if(problem_num >0,table_name,null)) table_num,
    sum(problem_num) problem_num,
    now() create_time
from governance_assess_table
where assess_date = '2023-05-26';

select id,
       assess_date,
       score_spec,
       score_storage,
       score_calc,
       score_quality,
       score_security,
       score,
       table_num,
       problem_num,
       create_time
from governance_assess_global;


select
       null id,
       '2023-05-26' assess_date,
       tec_owner,
       avg(score_spec_avg) score_spec,
       avg(score_storage_avg) score_storage,
       avg(score_calc_avg) score_calc,
       avg(score_quality_avg) score_quality,
       avg(score_security_avg) score_security,
       avg(score_on_type_weight) score,
       count(if(problem_num >0,table_name,null)) table_num,
       sum(problem_num) problem_num,
       now() create_time
from governance_assess_table
  where assess_date = '2023-05-26'
    and length(ifnull(tec_owner,'')) > 0
group by tec_owner;


-- governance_assess_detail粒度： 每天每张表每个指标一行
-- 每天每张表1行
-- 如果 a 和 c是1：1的关系 那么 group by a 和 group a,c是一样的效果，不会改变分组结果
select null,
       '2023-05-26' assess_date,
       table_name,
       schema_name,
       tec_owner,
       ifnull(avg(if(governance_type='SPEC',assess_score,null)),0) score_spec_avg,
       ifnull(avg(if(governance_type='STORAGE',assess_score,null)),0) score_storage_avg,
       ifnull(avg(if(governance_type='CALC',assess_score,null)),0) score_calc_avg,
       ifnull(avg(if(governance_type='QUALITY',assess_score,null)),0) score_quality_avg,
       ifnull(avg(if(governance_type='SECURITY',assess_score,null)),0) score_security_avg,
       null score_on_type_weight,
       count(if(assess_score < 10,assess_score,null)) problem_num,
       now() create_time
from governance_assess_detail
where assess_date = '2023-05-26'
group by schema_name,table_name,tec_owner;


-- 几个函数
-- timestampdiff(时间单位，时间1，时间2)。 时间必须是DateTime 或 Date
select timestampdiff(second ,start_time,end_time)
 from t_ds_task_instance
where id = 1;


-- 求日期差值  DATE_ADD|SUB(date, INTERVAL n DAY)
select date_sub('2023-08-28',INTERVAL 3 day );

insert into t_ds_task_instance
select
       null,
       name,
       task_type,
       task_code,
       task_definition_version,
       process_instance_id,
       state,
       submit_time,
       start_time,
       end_time,
       host,
       execute_path,
       log_path,
       alert_flag,
       retry_times,
       pid,
       app_link,
       task_params,
       flag,
       retry_interval,
       max_retry_times,
       task_instance_priority,
       worker_group,
       environment_code,
       environment_config,
       executor_id,
       first_submit_time,
       delay_time,
       var_pool,
       dry_run
from t_ds_task_instance
where name = 'gmall.dim_user_zip';


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


CREATE TABLE `governance_assess_detail` (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                            `assess_date` varchar(20) DEFAULT NULL COMMENT '考评日期',
                                            `table_name` varchar(200) DEFAULT NULL COMMENT '表名',
                                            `schema_name` varchar(200) DEFAULT NULL COMMENT '库名',
                                            `metric_id` varchar(200) DEFAULT NULL COMMENT '指标项id',
                                            `metric_name` varchar(200) DEFAULT NULL COMMENT '指标项名称',
                                            `governance_type` varchar(200) DEFAULT NULL COMMENT '治理类型',
                                            `tec_owner` varchar(200) DEFAULT NULL COMMENT '技术负责人',
                                            `assess_score` decimal(10,2) DEFAULT NULL COMMENT '考评得分',
                                            `assess_problem` varchar(2000) DEFAULT NULL COMMENT '考评问题项',
                                            `assess_comment` varchar(2000) DEFAULT NULL COMMENT '考评备注',
                                            `is_assess_exception` varchar(1) DEFAULT '0' COMMENT '考评是否异常',
                                            `assess_exception_msg` varchar(2000) DEFAULT NULL COMMENT '异常信息',
                                            `governance_url` varchar(2000) DEFAULT NULL COMMENT '治理处理路径',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                            PRIMARY KEY (`id`)
)
    COMMENT='治理考评结果明细';