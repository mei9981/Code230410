package com.atguigu.dga.assess.assessor;

import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.assess.bean.GovernanceMetric;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.bean.TableMetaInfoExtra;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;

/**
 * Created by Smexy on 2023/8/23
 */
public abstract class AssessorTemplate
{
    /*
        提供一个非抽象方法，定义总体流程

        参数中：需要提供相关的信息。
                a) 表的元数据信息
                b) 提供对应的指标(考题)
                c) 提供考评日期

        最终： 返回考评的结果
     */
    public GovernanceAssessDetail doAssess(TableMetaInfo tableMetaInfo, GovernanceMetric metric, String assessDate){

        TableMetaInfoExtra metaInfoExtra = tableMetaInfo.getTableMetaInfoExtra();
        GovernanceAssessDetail detail = new GovernanceAssessDetail();
        //填写可以直接获取的信息
        detail.setTableName(tableMetaInfo.getTableName());
        detail.setSchemaName(tableMetaInfo.getSchemaName());
        detail.setAssessDate(assessDate);
        detail.setMetricId(metric.getId().toString());
        detail.setMetricName(metric.getMetricName());
        detail.setGovernanceType(metric.getGovernanceType());
        detail.setTecOwner(metaInfoExtra.getTecOwnerUserName());
        detail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        detail.setGovernanceUrl(metric.getGovernanceUrl());
        //剩下的内容，需要执行考评，才能获取
        //具体考评步骤  赋值 score,comment,problem, 处理url
        try {
            assess(tableMetaInfo,metric,assessDate,detail);
        }catch (Exception e){
            detail.setIsAssessException("1");
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            //把堆栈报错信息，对接到printWriter
            e.printStackTrace(printWriter);
            //获取报错信息
            String exceptingMsg = writer.toString();
            detail.setAssessExceptionMsg(exceptingMsg.substring(0,Math.min(2000,exceptingMsg.length())));
            //TODO  新手完，开始直接把错误抛到控制台，让程序停下来，方便调试
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return detail;
    };

    //把业务核心流程声明为抽象方法，具体实现交给子类
    protected abstract void assess(TableMetaInfo tableMetaInfo, GovernanceMetric metric, String assessDate,GovernanceAssessDetail detail);
}
