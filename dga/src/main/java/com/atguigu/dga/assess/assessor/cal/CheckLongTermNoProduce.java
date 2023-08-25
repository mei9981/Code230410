package com.atguigu.dga.assess.assessor.cal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Smexy on 2023/8/25
 *
 *  一张表{days}天内没有产出数据  则给0分，其余给10
 *
 *      只要表产出数据，一定会向Hdfs写数据，只要写，就会更新表的 lastModifiedTime
 */
@Component("LONGTERM_NO_PRODUCE")
public class CheckLongTermNoProduce extends AssessorTemplate
{
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws URISyntaxException, Exception {
        //获取参数
        JSONObject params = JSON.parseObject(param.getMetric().getMetricParamsJson());
        Integer days = params.getInteger("days");
        //使用考评日期 和 最后一次修改时间进行比对
        Timestamp tableLastModifyTime = param.getTableMetaInfo().getTableLastModifyTime();
        String assessDate = param.getAssessDate();
        //把字符串的日期，转换为 Date类型的日期
        Date date = DateUtils.parseDate(assessDate, "yyyy-MM-dd");
        long diffMills = Math.abs(tableLastModifyTime.getTime() - date.getTime());
        //把差值换算为天
        long diffDays = TimeUnit.DAYS.convert(diffMills, TimeUnit.MILLISECONDS);

        //判断
        if (diffDays > days){
            assessScore(BigDecimal.ZERO,"超过"+diffDays+"天，没有产出数据","",detail,false,null);
        }

    }
}
