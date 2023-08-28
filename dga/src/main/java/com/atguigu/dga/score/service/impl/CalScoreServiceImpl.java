package com.atguigu.dga.score.service.impl;

import com.atguigu.dga.score.service.CalScoreService;
import com.atguigu.dga.score.service.GovernanceAssessGlobalService;
import com.atguigu.dga.score.service.GovernanceAssessTableService;
import com.atguigu.dga.score.service.GovernanceAssessTecOwnerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Smexy on 2023/8/28
 */
@Service
public class CalScoreServiceImpl implements CalScoreService
{
    @Autowired
    private GovernanceAssessTableService tableService;

    @Autowired
    private GovernanceAssessTecOwnerService ownerService;

    @Autowired
    private GovernanceAssessGlobalService globalService;


    @Override
    public void calScore(String assessDate) throws Exception {
        //计算每张表的得分
        tableService.calScorePerTable(assessDate);
        //计算每个人的得分
        ownerService.calScoreByTecOwner(assessDate);
        //计算总分
        globalService.calGlobalScore(assessDate);
    }
}
