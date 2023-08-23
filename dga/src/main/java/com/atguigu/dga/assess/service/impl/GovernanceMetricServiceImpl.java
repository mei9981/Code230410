package com.atguigu.dga.assess.service.impl;

import com.atguigu.dga.assess.bean.GovernanceMetric;
import com.atguigu.dga.assess.mapper.GovernanceMetricMapper;
import com.atguigu.dga.assess.service.GovernanceMetricService;
import com.atguigu.dga.meta.service.TableMetaInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考评指标参数表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-23
 */
@Service
public class GovernanceMetricServiceImpl extends ServiceImpl<GovernanceMetricMapper, GovernanceMetric> implements GovernanceMetricService {

    @Autowired
    private TableMetaInfoService metaInfoService;
    /*
        1.考生进考场
                考生: 数仓中的表的元数据信息
                查询今天要参与考评的表的元数据信息

              从table_meta_info 和 table_meta_info_extra查询今天要参与测评的所有表的元数据信息。

        2.发试卷考试
                试卷:  测评指标集合
                考题:  一个测评指标

        3.收集结果，押运，判分
                把每一张表考试的结果，存入数据库。
     */
    @Override
    public void assess(String assessDate) {

    }
}
