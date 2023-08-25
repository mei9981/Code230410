package com.atguigu.dga.assess.assessor.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.Field;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.config.MetaInfoUtil;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Smexy on 2023/8/25
 *
 *  防止建模不规范，代理冗余存储
 *
 * 同层次两个表字段重复(字段名+注释)超过{percent}%，则给0分，其余给10分
 */
@Component("TABLE_SIMILAR")
public class CheckTableSimiliarAssessor extends AssessorTemplate
{
    @Autowired
    private MetaInfoUtil metaInfoUtil;
    /*
        重复口径:  字段名_注释
     */
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) {

        Map<String, TableMetaInfo> tableMetaInfoMap = metaInfoUtil.tableMetaInfoMap;
        //获取参数
        Integer limtPercent = JSON.parseObject(param.getMetric().getMetricParamsJson()).getInteger("percent");
        //获取当前表的字段信息
        String currentTableFieldJson = param.getTableMetaInfo().getColNameJson();
        //获取当前表的层级
        String dwLevel = param.getTableMetaInfo().getTableMetaInfoExtra().getDwLevel();
        //获取当前表的完整名字  库名.表名
        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        String key = tableMetaInfo.getSchemaName() + "." + tableMetaInfo.getTableName();
        //使用当前表，和当前层的其他表进行比对
        Set<Map.Entry<String, TableMetaInfo>> entries = tableMetaInfoMap.entrySet();

        //声明一个集合，存储交集的记录。当前表和每一张表的交集结果都记录在一个JSONObject中
        List<JSONObject> result = new ArrayList<>();

        for (Map.Entry<String, TableMetaInfo> entry : entries) {
            //之和同层的表对比，不和自己比
            if (!entry.getKey().equals(key) && entry.getValue().getTableMetaInfoExtra().getDwLevel().equals(dwLevel) ){
                //进行字段的比对
                //获取当前表的字段集合
                Set<String> currentTableFieldsSet = extractFieldNames(currentTableFieldJson);
                int fieldNum = currentTableFieldsSet.size();
                Set<String> comparedTableFieldsSet = extractFieldNames(entry.getValue().getColNameJson());
                //产生了交集
                if (currentTableFieldsSet.retainAll(comparedTableFieldsSet)) {
                    BigDecimal percent = BigDecimal.valueOf(currentTableFieldsSet.size())
                                                  .divide(BigDecimal.valueOf(fieldNum), 2, RoundingMode.HALF_UP)
                                                    .movePointRight(2);

                    //超过建议的阈值
                    if (percent.compareTo(BigDecimal.valueOf(limtPercent)) == 1){
                        JSONObject jsonObject = new JSONObject();
                        //记录重复的信息
                        jsonObject.put("compareTable",entry.getKey());
                        jsonObject.put("percent",percent);
                        jsonObject.put("fieldDetail",JSON.toJSONString(currentTableFieldsSet));
                        result.add(jsonObject);
                    }
                }

            }
        }

        //打分
        if (!result.isEmpty()){
            assessScore(BigDecimal.ZERO,"和其他表存在相似字段",JSON.toJSONString(result),detail,false,null);
        }
    }

    //将字段的json，转换为Set<String>
    private Set<String> extractFieldNames(String json){

        List<Field> fields = JSON.parseArray(json, Field.class);

        //把每个字段按照 字段名_注释 进行处理
        Set<String> filedSet = fields.stream().map(f -> f.getName() + "_" + f.getComment()).collect(Collectors.toSet());

        return filedSet;
    }
}
