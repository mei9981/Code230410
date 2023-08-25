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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Smexy on 2023/8/25
 *
 *  一张表{days}天内没有访问 则给0分 ， 其余给10
 *
 *
 */
@Component("LONGTERM_NO_ACCESS")
public class CheckLongTermNoAccess extends AssessorTemplate
{
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws URISyntaxException, Exception {
        //获取参数
        JSONObject params = JSON.parseObject(param.getMetric().getMetricParamsJson());
        Integer days = params.getInteger("days");
        //求最近一次的访问时间,把ts转换为时间(新的api LocalDateTime)
        Timestamp lastAccessTime = param.getTableMetaInfo().getTableLastAccessTime();
        LocalDateTime lastAccessTimeLDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastAccessTime.getTime()), ZoneId.of("Asia/Shanghai"));
        /*
            如果符合条件的话，求一个极限时间。假设days=3
                考评日期: 2023-08-25日，如果符合条件，上一次访问时间至少要大于 2023-08-25 - 3

                LocalDate： 只有日期部分，没有时间部分。 2023-08-25
                LocalDateTime： 有日期部分，有时间部分。 2023-08-25 01:01:01
         */
        //需要把字符串的2023-08-25,转换为LocalDateTime对象
        LocalDate assessDateLD = LocalDate.parse(param.getAssessDate());
        //在 日期后添加 00:00:00
        LocalDateTime assessDateLDT = assessDateLD.atStartOfDay();
        LocalDateTime limitLDT = assessDateLDT.plusDays(days);

        //判断  上次访问时间 早于 符合要求的极限时间
        if (lastAccessTimeLDT.isBefore(limitLDT)){
            assessScore(BigDecimal.ZERO,"超过"+days+"没有访问...","",detail,false,null);
        }

    }
}
