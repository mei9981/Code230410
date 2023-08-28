package com.atguigu.dga.score.service.impl;

import com.atguigu.dga.score.bean.GovernanceType;
import com.atguigu.dga.score.mapper.GovernanceTypeMapper;
import com.atguigu.dga.score.service.GovernanceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 治理考评类别权重表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Service
public class GovernanceTypeServiceImpl extends ServiceImpl<GovernanceTypeMapper, GovernanceType> implements GovernanceTypeService {

    @Override
    public Map<String, BigDecimal> getWeightMap() {
        //先查询权重
        List<GovernanceType> weightData = list();

        Map<String, BigDecimal> weightMap = new HashMap<>();

        for (GovernanceType weight : weightData) {
            weightMap.put(weight.getTypeCode(),weight.getTypeWeight());
        }

        return weightMap;
    }
}
