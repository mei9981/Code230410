package com.atguigu.dga.score.service;

import com.atguigu.dga.score.bean.GovernanceType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 治理考评类别权重表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
public interface GovernanceTypeService extends IService<GovernanceType> {

    Map<String, BigDecimal> getWeightMap();
}
