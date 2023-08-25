package com.atguigu.dga.assess.assessor.secutiry;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.config.MetaConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Smexy on 2023/8/25
 */
@Component("IS_SAFE_LEVEL_SET")
public class CheckSecurityLevelAssessor extends AssessorTemplate
{
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {
        String securityLevel = param.getTableMetaInfo().getTableMetaInfoExtra().getSecurityLevel();
        if (StringUtils.isBlank(securityLevel) || MetaConstant.SECURITY_LEVEL_UNSET.equals(securityLevel)){
            assessScore(BigDecimal.ZERO,"未生命安全级别","",detail,true,param.getTableMetaInfo().getId().toString());
        }
    }
}
