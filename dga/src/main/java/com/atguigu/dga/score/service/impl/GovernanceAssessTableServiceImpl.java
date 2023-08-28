package com.atguigu.dga.score.service.impl;

import com.atguigu.dga.score.bean.GovernanceAssessTable;
import com.atguigu.dga.score.bean.GovernanceType;
import com.atguigu.dga.score.mapper.GovernanceAssessTableMapper;
import com.atguigu.dga.score.service.GovernanceAssessTableService;
import com.atguigu.dga.score.service.GovernanceTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 表治理考评情况 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-28
 */
@Service
public class GovernanceAssessTableServiceImpl extends ServiceImpl<GovernanceAssessTableMapper, GovernanceAssessTable> implements GovernanceAssessTableService {

    @Autowired
    private GovernanceTypeService weightService;
    /*
        1.为了避免数据重复，先删除今天已经写入的数据
        2.算分
        3.写入
     */
    @Override
    public void calScorePerTable(String assessDate) throws Exception {

        //1.先删除今天的数据
        remove(new QueryWrapper<GovernanceAssessTable>().eq("assess_date",assessDate));

        //2.算分
        List<GovernanceAssessTable> governanceAssessTables = baseMapper.calScorePerTable(assessDate);
        //计算加权分数
        Map<String, BigDecimal> weightMap = weightService.getWeightMap();

        /*
            根据类型，取出GovernanceAssessTable中的平均得分。
                CACL, GovernanceAssessTable.getScoreCalcAvg() * weightMap.get("CACL")
                    +
                    .....;
         */
        //声明加权后的总分
        Set<String> weightType = weightMap.keySet();
        /*
            type: CACL
                调用一个 对象的 getScoreCalcAvg()方法
         */
        governanceAssessTables.stream().forEach(
            t -> {
                BigDecimal  scoreOnTypeWeight = BigDecimal.ZERO;
                for (String type : weightType) {
                    try {
                        scoreOnTypeWeight = scoreOnTypeWeight.add(callGetterByStr(type,t).multiply(weightMap.get(type)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                //假设总分还是10分
                t.setScoreOnTypeWeight(scoreOnTypeWeight.movePointLeft(1));
            }
        );

        //存入数据库
        saveBatch(governanceAssessTables);


    }

    /*
        根据 指标类型，获取GovernanceAssessTable 对象中，指定类型的平均分。
     */
    private BigDecimal callGetterByStr(String type,GovernanceAssessTable governanceAssessTable) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String methodName = "getScore" + type.substring(0, 1) + type.substring(1).toLowerCase() + "Avg";
        //调用一个对象指定的方法，需要使用反射，获取方法对象
        Class<? extends GovernanceAssessTable> clazz = governanceAssessTable.getClass();
        Method method = clazz.getMethod(methodName);
        //执行
        BigDecimal val = (BigDecimal)method.invoke(governanceAssessTable);
        return val;
    }
}
