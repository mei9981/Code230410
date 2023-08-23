package com.atguigu.dga.assess.assessor.spec;

import com.alibaba.fastjson.JSON;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.Field;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Smexy on 2023/8/23
 */
@Component("HAVE_FIELD_COMMENT")
public class CheckFieldCommentAssessor extends AssessorTemplate
{

    /*
        有备注字段/所有字段 *10分
     */
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {

        //获取所有的表的字段
        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        List<Field> fields = JSON.parseArray(tableMetaInfo.getColNameJson(), Field.class);
        //获取没有注释的字段数   集合操作，都用stream
        List<String> noCommentFieldNames = fields.stream()
                                     .filter(f -> StringUtils.isBlank(f.getComment()))
                                     .map(f -> f.getName())
                                     .collect(Collectors.toList());

        //打分
        if (!noCommentFieldNames.isEmpty()){
            BigDecimal score = BigDecimal.valueOf(noCommentFieldNames.size())
                                         .divide(BigDecimal.valueOf(fields.size()), 2, RoundingMode.HALF_UP)
                                         .movePointRight(1);
            assessScore(score,"部分字段未填写注释",JSON.toJSONString(noCommentFieldNames),detail,false,null);
        }
    }
}
