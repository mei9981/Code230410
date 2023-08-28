package com.atguigu.dga.score.service;

import com.atguigu.dga.score.bean.GovernanceAssessGlobal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 治理总考评表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
public interface GovernanceAssessGlobalService extends IService<GovernanceAssessGlobal> {

    void calGlobalScore(String assessDate);
}
