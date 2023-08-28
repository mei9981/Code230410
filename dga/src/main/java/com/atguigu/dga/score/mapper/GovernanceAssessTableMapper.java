package com.atguigu.dga.score.mapper;

import com.atguigu.dga.score.bean.GovernanceAssessTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 表治理考评情况 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Mapper
public interface GovernanceAssessTableMapper extends BaseMapper<GovernanceAssessTable> {

    @Select(" select null," +
        "       #{dt} assess_date," +
        "       table_name," +
        "       schema_name," +
        "       tec_owner," +
        "       ifnull(avg(if(governance_type='SPEC',assess_score,null)),0) score_spec_avg," +
        "       ifnull(avg(if(governance_type='STORAGE',assess_score,null)),0) score_storage_avg," +
        "       ifnull(avg(if(governance_type='CALC',assess_score,null)),0) score_calc_avg," +
        "       ifnull(avg(if(governance_type='QUALITY',assess_score,null)),0) score_quality_avg," +
        "       ifnull(avg(if(governance_type='SECURITY',assess_score,null)),0) score_security_avg," +
        "       null score_on_type_weight," +
        "       count(if(assess_score < 10,assess_score,null)) problem_num," +
        "       now() create_time" +
        " from governance_assess_detail" +
        " where assess_date = #{dt}" +
        " group by schema_name,table_name,tec_owner; ")
    List<GovernanceAssessTable> calScorePerTable(@Param("dt") String assessDate);
}
