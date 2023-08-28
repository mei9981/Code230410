package com.atguigu.dga.score.service.impl;

import com.atguigu.dga.score.bean.GovernanceAssessTable;
import com.atguigu.dga.score.bean.GovernanceAssessTecOwner;
import com.atguigu.dga.score.mapper.GovernanceAssessTecOwnerMapper;
import com.atguigu.dga.score.service.GovernanceAssessTecOwnerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 技术负责人治理考评表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Service
public class GovernanceAssessTecOwnerServiceImpl extends ServiceImpl<GovernanceAssessTecOwnerMapper, GovernanceAssessTecOwner> implements GovernanceAssessTecOwnerService {

    @Override
    public void calScoreByTecOwner(String assessDate) {

        remove(new QueryWrapper<GovernanceAssessTecOwner>().eq("assess_date",assessDate));

        List<GovernanceAssessTecOwner> governanceAssessTecOwners = baseMapper.calScoreByTecOwner(assessDate);

        saveBatch(governanceAssessTecOwners);

    }
}
