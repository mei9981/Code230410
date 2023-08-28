package com.atguigu.dga;

import com.atguigu.dga.score.service.CalScoreService;
import com.atguigu.dga.score.service.GovernanceAssessGlobalService;
import com.atguigu.dga.score.service.GovernanceAssessTableService;
import com.atguigu.dga.score.service.GovernanceAssessTecOwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Smexy on 2023/8/28
 */
@SpringBootTest
public class ScoreTest
{
    @Autowired
    private GovernanceAssessTableService tableService;
    @Test
    void saveTableScore() throws Exception {
        tableService.calScorePerTable("2023-05-26");
    }

    @Autowired
    private GovernanceAssessTecOwnerService ownerService;

    @Test
    void saveTecOwnnerScore() throws Exception {
        ownerService.calScoreByTecOwner("2023-05-26");
    }

    @Autowired
    private GovernanceAssessGlobalService globalService;

    @Test
    void saveGlobalScore() throws Exception {
        globalService.calGlobalScore("2023-05-26");
    }

    @Autowired
    private CalScoreService calScoreService;

    @Test
    void calScore() throws Exception {
        calScoreService.calScore("2023-05-26");
    }
}
