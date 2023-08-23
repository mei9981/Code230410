package com.atguigu.dga.assess.assessor.spec;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Smexy on 2023/8/23
 */
@Component("HAVE_TABLE_COMMENT")
public class CheckTableCommentAssessor extends AssessorTemplate
{

    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {

        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();

        if (StringUtils.isBlank(tableMetaInfo.getTableComment())){
            assessScore(BigDecimal.ZERO,"表缺少注释","",detail,false,null);
        }

    }
}
