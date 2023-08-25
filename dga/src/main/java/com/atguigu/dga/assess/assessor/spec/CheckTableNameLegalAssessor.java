package com.atguigu.dga.assess.assessor.spec;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.meta.bean.TableMetaInfoExtra;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Smexy on 2023/8/23
 *
 * 参考建数仓表规范
 * ODS层 ：开头:ods  结尾 :inc/full
 * 结构ods_xx_( inc|full)
 * DIM层 :  dim开头     full/zip 结尾
 * 结构: dim_xx_( full/zip)
 * DWD层:  dwd 开头  inc/full 结尾
 * 结构： dwd_xx_xx_(inc|full)
 * DWS层： dws开头
 * 结构dws_xx_xx_xx_ (1d/nd/td)
 * ADS层： ads 开头
 * 结构 ads_xxx
 * DM层： dm开头
 * 结构: dm_xx
 * 符合则 10分，否则0分
 * OTHER：
 * 未纳入分层，给5分
 */
@Component("TABLE_NAME_STANDARD")
public class CheckTableNameLegalAssessor extends AssessorTemplate
{

    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {
        //获取表名，分层
        TableMetaInfoExtra tableMetaInfoExtra = param.getTableMetaInfo().getTableMetaInfoExtra();
        String dwLevel = tableMetaInfoExtra.getDwLevel();
        String schemaName = tableMetaInfoExtra.getSchemaName();
        String tableName = tableMetaInfoExtra.getTableName();
        String id = param.getTableMetaInfo().getId().toString();


        if (dwLevel.equals(MetaConstant.DW_LEVEL_OTHER)){
            //给5分
            assessScore(BigDecimal.valueOf(5),"未纳入分层","",detail,true,id);
        }else if (StringUtils.isBlank(dwLevel) || dwLevel.equals(MetaConstant.DW_LEVEL_UNSET)){
            assessScore(BigDecimal.ZERO,"未填写表的分层信息","",detail,true,id);
        }else if ("gmall".equals(schemaName)){
            switch (dwLevel){
                case MetaConstant.DW_LEVEL_ODS: isMatch(tableName,MetaConstant.GMALL_ODS_REGEX,detail,id); break;
                case MetaConstant.DW_LEVEL_DIM: isMatch(tableName,MetaConstant.GMALL_DIM_REGEX,detail,id); break;
                case MetaConstant.DW_LEVEL_DWD: isMatch(tableName,MetaConstant.GMALL_DWD_REGEX,detail,id); break;
                case MetaConstant.DW_LEVEL_DWS: isMatch(tableName,MetaConstant.GMALL_DWS_REGEX,detail,id); break;
                case MetaConstant.DW_LEVEL_ADS: isMatch(tableName,MetaConstant.GMALL_ADS_REGEX,detail,id); break;
                case MetaConstant.DW_LEVEL_DM: isMatch(tableName,MetaConstant.GMALL_DM_REGEX,detail,id); break;
            }
        }

    }

    //编写方法，使用正则判断分层名是否合规
    private void isMatch(String table,String regex,GovernanceAssessDetail detail,String id){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(table);
        if (!matcher.matches()){
         assessScore(BigDecimal.ZERO,"表名不符合分层信息","",detail,true,id);
        }
    }
}
