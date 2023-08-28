package com.atguigu.dga.config;

import com.atguigu.dga.assess.service.GovernanceAssessDetailService;
import com.atguigu.dga.score.service.CalScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

/**
 * Created by Smexy on 2023/8/28
 */
@Component
public class ScoreTask
{
    @Value("${assess.dbs}")
    private String dbs;

    @Autowired
    private CalScoreService calScoreService;

    @Autowired
    private GovernanceAssessDetailService detailService;

    @Scheduled(cron = "0 52 14 * * *")
    public Object reCalScore() throws Exception {

        //获取当天日期
        //String assessDate = LocalDate.now().toString();
        String assessDate = "2023-05-26";

        System.out.println("开始执行考评...");

        String[] dbNames = dbs.split(",");

        for (String dbName : dbNames) {
            //考评
            detailService.assess(dbName,assessDate);
            //重新计算得分
            calScoreService.calScore(assessDate);
        }

        return "success";
    }
}
