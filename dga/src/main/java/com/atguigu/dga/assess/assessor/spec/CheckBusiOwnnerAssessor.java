package com.atguigu.dga.assess.assessor.spec;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.bean.TableMetaInfoExtra;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Smexy on 2023/8/23
 */
@Component("HAVE_BUS_OWNER")
public class CheckBusiOwnnerAssessor extends AssessorTemplate
{
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {

        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        TableMetaInfoExtra tableMetaInfoExtra = tableMetaInfo.getTableMetaInfoExtra();

        if (StringUtils.isBlank(tableMetaInfoExtra.getBusiOwnerUserName())){
            //打0分
            assessScore(BigDecimal.ZERO,"没有指定业务负责人","",detail,true,tableMetaInfo.getId().toString());
        }
    }
}
