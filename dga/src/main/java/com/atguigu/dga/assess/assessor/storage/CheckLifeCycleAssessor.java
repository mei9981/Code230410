package com.atguigu.dga.assess.assessor.storage;

import com.alibaba.fastjson.JSON;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.bean.TableMetaInfoExtra;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Smexy on 2023/8/25
 *
 * 未设定周期类型的 给 0分
 * 周期类型为永久、拉链表 则给10分
 * 周期类型为日分区 :
 * 无分区信息的给0分
 *             没设生命周期天数给0分
 * 周期长度超过建议周期天数{days}给5分
 */
@Component("LIFECYCLE_ELIGIBLE")
public class CheckLifeCycleAssessor extends AssessorTemplate
{
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {
        //获取参数
        Integer recommandDays = JSON.parseObject(param.getMetric().getMetricParamsJson()).getInteger("days");
        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        TableMetaInfoExtra tableMetaInfoExtra = tableMetaInfo.getTableMetaInfoExtra();
        //获取表当前的生命周期类型
        String lifecycleType = tableMetaInfoExtra.getLifecycleType();
        //判断过程
        if (MetaConstant.LIFECYCLE_TYPE_UNSET.equals(lifecycleType)){
            assessScore(BigDecimal.ZERO,"未设置生命周期类型","",detail,true,tableMetaInfo.getId().toString());
        }else if (MetaConstant.LIFECYCLE_TYPE_DAY.equals(lifecycleType)){
            //无分区信息的给0分
            if ("[]".equals(tableMetaInfo.getPartitionColNameJson())){
                assessScore(BigDecimal.ZERO,"日分区类型表设置不合理","表没有分区列",detail,true,tableMetaInfo.getId().toString());
            }
            if (tableMetaInfoExtra.getLifecycleDays() == -1){
                assessScore(BigDecimal.ZERO,"日分区类型表设置不合理","未设置分区信息保存天数",detail,true,tableMetaInfo.getId().toString());
            }else if (tableMetaInfoExtra.getLifecycleDays() > recommandDays){
                assessScore(BigDecimal.valueOf(5),"日分区类型表设置不合理","周期长度超过建议周期天数:"+recommandDays ,detail,true,tableMetaInfo.getId().toString());
            }

        }

    }
}
