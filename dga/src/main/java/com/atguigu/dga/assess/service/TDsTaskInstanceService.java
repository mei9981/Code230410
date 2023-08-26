package com.atguigu.dga.assess.service;

import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-25
 */
public interface TDsTaskInstanceService extends IService<TDsTaskInstance> {

    //编写方法查询今天要考评的表的Task执行的元数据信息
    List<TDsTaskInstance> getAllTaskInstances(String assessDate, Set<String> names);
}
