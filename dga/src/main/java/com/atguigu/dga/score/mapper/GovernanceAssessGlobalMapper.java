package com.atguigu.dga.score.mapper;

import com.atguigu.dga.score.bean.GovernanceAssessGlobal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 治理总考评表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Mapper
public interface GovernanceAssessGlobalMapper extends BaseMapper<GovernanceAssessGlobal> {

    @Select("select" +
        "    null id," +
        "    #{dt} assess_date," +
        "    avg(score_spec_avg) score_spec," +
        "    avg(score_storage_avg) score_storage," +
        "    avg(score_calc_avg) score_calc," +
        "    avg(score_quality_avg) score_quality," +
        "    avg(score_security_avg) score_security," +
        "    avg(score_on_type_weight) score," +
        "    count(if(problem_num >0,table_name,null)) table_num," +
        "    sum(problem_num) problem_num," +
        "    now() create_time" +
        " from governance_assess_table" +
        " where assess_date = #{dt};")
    GovernanceAssessGlobal calGlobalScore(@Param("dt") String assessDate);
}
