package com.atguigu.dga.score.mapper;

import com.atguigu.dga.score.bean.GovernanceAssessTecOwner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 技术负责人治理考评表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Mapper
public interface GovernanceAssessTecOwnerMapper extends BaseMapper<GovernanceAssessTecOwner> {

    @Select("select" +
        "       null id," +
        "       #{dt} assess_date," +
        "       tec_owner," +
        "       avg(score_spec_avg) * 10 score_spec," +
        "       avg(score_storage_avg) * 10 score_storage," +
        "       avg(score_calc_avg) * 10 score_calc," +
        "       avg(score_quality_avg) * 10 score_quality," +
        "       avg(score_security_avg) * 10 score_security," +
        "       avg(score_on_type_weight) score," +
        "       count(if(problem_num >0,table_name,null)) table_num," +
        "       sum(problem_num) problem_num," +
        "       now() create_time" +
        " from governance_assess_table" +
        "  where assess_date = #{dt}" +
        "    and length(ifnull(tec_owner,'')) > 0" +
        " group by tec_owner;")
    List<GovernanceAssessTecOwner> calScoreByTecOwner(@Param("dt") String assessDate);
}
