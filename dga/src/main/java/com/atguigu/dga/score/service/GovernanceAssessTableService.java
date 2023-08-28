package com.atguigu.dga.score.service;

import com.atguigu.dga.score.bean.GovernanceAssessTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * 表治理考评情况 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
public interface GovernanceAssessTableService extends IService<GovernanceAssessTable> {

    //计算每张表的得分，写入数据库
    void  calScorePerTable(String assessDate) throws Exception;
}
