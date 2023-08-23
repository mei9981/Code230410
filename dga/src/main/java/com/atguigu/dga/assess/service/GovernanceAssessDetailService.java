package com.atguigu.dga.assess.service;

import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 治理考评结果明细 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-23
 */
public interface GovernanceAssessDetailService extends IService<GovernanceAssessDetail> {

    //每日考评
    void assess(String db,String assessDate);
}
