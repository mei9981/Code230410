package com.atguigu.dga.assess.assessor.quality;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.atguigu.dga.assess.service.TDsTaskInstanceService;
import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by Smexy on 2023/8/28
 *
 * 前一天数据产出(导数，计算)时效，超过前{days}天产出时效平均值n%
 *    则给0分，其余10分。
 *    t_ds_task_instance
 *    1.计算今天数据产生的时效
 *    2.计算前{days}天产出时效
 *    3.对比，看是否超过 n%
 */
@Component("TABLE_PRODUCE_EFFICIENCY")
public class CheckTableEfficiencyAssessor extends AssessorTemplate
{
    @Autowired
    private TDsTaskInstanceService taskInstanceService;
    /*
        使用timestampdiff(second ,start_time,end_time)求时效
     */
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws URISyntaxException, Exception {

        //获取参数
        JSONObject params = JSON.parseObject(param.getMetric().getMetricParamsJson());
        Integer days = params.getInteger("days");
        Integer percent = params.getInteger("n");

        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        String tableName = tableMetaInfo.getTableName();
        String schemaName = tableMetaInfo.getSchemaName();

        //拼接生成task的名字
        String taskName = schemaName + "." + tableName;
        //计算今天任务的时效
        QueryWrapper<TDsTaskInstance> queryWrapper = new QueryWrapper<TDsTaskInstance>()
            .eq("date(start_time)", param.getAssessDate())
            .eq("state", MetaConstant.TASK_STATE_SUCCESS)
            .eq("name", taskName)
            .select("timestampdiff(second ,start_time,end_time) sec");

        Integer sec = (Integer) taskInstanceService.getMap(queryWrapper).get("sec");

        String assessDate = param.getAssessDate();
        String daysBeforeDate = LocalDate.parse(assessDate).minusDays(days).toString();
        //前{days}天产出时效平均值    date(start_time) > date_sub(当前考评日期,days)
        QueryWrapper<TDsTaskInstance> queryWrapper2 = new QueryWrapper<TDsTaskInstance>()
            //使用gt，必须使用java代码计算前n天的日期
            .ge("date(start_time)", daysBeforeDate)
            .lt("date(start_time)",assessDate)
            .eq("state", MetaConstant.TASK_STATE_SUCCESS)
            .eq("name", taskName)
            .select("avg(timestampdiff(second ,start_time,end_time)) avgSec")
            //拼接sql
            //.last(" where date(start_time) > date_sub('"+param.getAssessDate()+"',INTERVAL "+days+" day ) and date(start_time) < "+"'"+param.getAssessDate()+"'")
            ;
        Double avgSec = (Double) taskInstanceService.getMap(queryWrapper2).get("avgSec");

        //判断，avgSec有可能为null
        if (avgSec == null){
            //当前只调度了一天，不用比
            return;
        }

        BigDecimal limit = BigDecimal.valueOf(avgSec).multiply(BigDecimal.valueOf(100 + percent)).movePointLeft(2);

        //今天的时效超过阈值
        if (BigDecimal.valueOf(sec).compareTo(limit) == 1 ){
            assessScore(BigDecimal.ZERO,"时效超过阈值","今天运行的时效:"+sec +",超过了过去"+days+"时效的均值:"+avgSec+"的"+percent,detail,false,null);
        }


    }
}
