package com.atguigu.dga.assess.service.impl;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.assessor.spec.CheckBusiOwnnerAssessor;
import com.atguigu.dga.assess.assessor.spec.CheckTecOwnnerAssessor;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.assess.bean.GovernanceMetric;
import com.atguigu.dga.assess.mapper.GovernanceAssessDetailMapper;
import com.atguigu.dga.assess.service.GovernanceAssessDetailService;
import com.atguigu.dga.assess.service.GovernanceMetricService;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.service.TableMetaInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 治理考评结果明细 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-23
 */
@Service
public class GovernanceAssessDetailServiceImpl extends ServiceImpl<GovernanceAssessDetailMapper, GovernanceAssessDetail> implements GovernanceAssessDetailService {

    @Autowired
    private TableMetaInfoService metaInfoService;

    @Autowired
    private GovernanceMetricService metricService;

    @Autowired
    private ApplicationContext context;
    /*
        1.考生进考场
                考生: 数仓中的表的元数据信息
                查询今天要参与考评的表的元数据信息

              从table_meta_info 和 table_meta_info_extra查询今天要参与测评的所有表的元数据信息。

        2.发试卷考试
                试卷:  测评指标  GovernanceAssessDetail
                考题:  一个测评指标  GovernanceMetric

                每一张表，都要考评17个指标

                assessor: 阅卷老师，判断题应该得多少分

        3.收集结果，押运，判分
                把每一张表考试的结果，存入数据库。
                List<GovernanceAssessDetail>
     */
    @Override
    public void assess(String db,String assessDate) {

        //待考评的表的元数据
        List<TableMetaInfo> tableMetaInfos = metaInfoService.queryTableMetaInfo(db, assessDate);

        //查询今天要考评的指标
        List<GovernanceMetric> metrics = metricService.list(
            new QueryWrapper<GovernanceMetric>()
                .eq("is_disabled", "否")
        );

        //遍历
        for (TableMetaInfo tableMetaInfo : tableMetaInfos) {
            for (GovernanceMetric metric : metrics) {
                /*
                    完成考试逻辑
                    判断当前这道题是什么题，把这个题交给对应的批卷老师(assessor)
                 */
                /*if (metric.getMetricCode().equals("HAVE_TEC_OWNER")){
                    new CheckTecOwnnerAssessor().assess();
                }else if (metric.getMetricCode().equals("HAVE_BUS_OWNER")){
                    new CheckBusiOwnnerAssessor().assess();
                }*/
                /*
                    ... 17个if判断.能解决问题，维护性差。
                     违背了软件开发的一个原则：
                        开闭原则：
                            开:  增加新功能时，不能去修改之前的代码。
                            闭:  删除旧功能时，不能去修改之前的代码。

                      设计模式:
                            开发软件时，设计功能时，常常使用的一些固定的套路。
                            套路针对某些特定的场景设计的，可以解决一些特定的问题。

                          模版模式。可以解决这个问题。
                 */

                //使用模版父类对象，来执行方法。 为父类对象提供子类实现。
                AssessorTemplate assessor = context.getBean(AssessorTemplate.class,metric.getMetricCode());

                assessor.doAssess(tableMetaInfo);

            }
        }

    }
}
