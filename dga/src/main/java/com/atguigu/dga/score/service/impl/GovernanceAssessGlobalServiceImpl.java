package com.atguigu.dga.score.service.impl;

import com.atguigu.dga.score.bean.GovernanceAssessGlobal;
import com.atguigu.dga.score.bean.GovernanceAssessTecOwner;
import com.atguigu.dga.score.mapper.GovernanceAssessGlobalMapper;
import com.atguigu.dga.score.service.GovernanceAssessGlobalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 治理总考评表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Service
public class GovernanceAssessGlobalServiceImpl extends ServiceImpl<GovernanceAssessGlobalMapper, GovernanceAssessGlobal> implements GovernanceAssessGlobalService {

    @Override
    public void calGlobalScore(String assessDate) {

        remove(new QueryWrapper<GovernanceAssessGlobal>().eq("assess_date",assessDate));

        GovernanceAssessGlobal governanceAssessGlobal = baseMapper.calGlobalScore(assessDate);

        save(governanceAssessGlobal);
    }
}
