package com.atguigu.dga.assess.assessor.storage;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Smexy on 2023/8/25
 */
@Component("IS_EMPTY_TABLE")
public class CheckEmptyTableAssessor extends AssessorTemplate
{
    //表中有没有数据
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {
        Long tableSize = param.getTableMetaInfo().getTableSize();
        if (tableSize == 0){
            assessScore(BigDecimal.ZERO,"当前表是空表","",detail,false,null);
        }
    }
}
