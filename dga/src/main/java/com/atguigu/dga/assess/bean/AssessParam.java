package com.atguigu.dga.assess.bean;

import com.atguigu.dga.meta.bean.TableMetaInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Smexy on 2023/8/23
 */
//封装考评需要的参数
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
public class AssessParam
{
    private TableMetaInfo tableMetaInfo;
    private  GovernanceMetric metric ;
    private String assessDate;
}
