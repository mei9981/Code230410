package com.atguigu.dga.config;

//避免被实例化
public interface MetaConstant
{
    //定义正则表达式，验证每一层的命名规则
    String GMALL_ODS_REGEX = "^ods_\\w+_(inc|full)$";
    String GMALL_DIM_REGEX = "^dim_\\w+_(zip|full)$";
    String GMALL_DWD_REGEX = "^dwd_(trade|tool|interaction|traffic|user)_\\w+_(inc|full|acc)$";
    // 1d,nd,td
    String GMALL_DWS_REGEX = "^dws_(trade|tool|interaction|traffic|user)_\\w+_(\\d+d|nd|td)$";
    String GMALL_ADS_REGEX = "^ads_\\w+$";
    String GMALL_DM_REGEX = "^dm_\\w+$";
    String schema_name="";
    //存储周期
    String LIFECYCLE_TYPE_PERM="PERM";  //永久
    String LIFECYCLE_TYPE_ZIP="ZIP";   //拉链
    String LIFECYCLE_TYPE_DAY="DAY";  //日分区
    String LIFECYCLE_TYPE_OTHER="OTHER";  //其他
    String LIFECYCLE_TYPE_UNSET="UNSET";  //未设置

    //安全级别
    String SECURITY_LEVEL_UNSET="UNSET";  //未设置
    String SECURITY_LEVEL_PUBLIC="PUBLIC";  //公开
    String SECURITY_LEVEL_INTERNAL="INTERNAL";  //内部
    String SECURITY_LEVEL_SECRET="SECRET";  //保密
    String SECURITY_LEVEL_HIGH="HIGH";  //高度机密

    //层级 对应页面上的下拉框中的选项
    String DW_LEVEL_UNSET = "UNSET";
    String DW_LEVEL_ODS = "ODS";
    String DW_LEVEL_DWD = "DWD";
    String DW_LEVEL_DWS = "DWS";
    String DW_LEVEL_DIM = "DIM";
    //Data Market 一张表中存放的数据很杂，不属于以上层，放到DM
    String DW_LEVEL_DM = "DM";
    String DW_LEVEL_ADS = "ADS";
    //表不属于以上的任意一层
    String DW_LEVEL_OTHER = "OTHER";

    //DS状态码
    Integer TASK_STATE_SUCCESS = 7;
    Integer TASK_STATE_FAILED = 6;
}