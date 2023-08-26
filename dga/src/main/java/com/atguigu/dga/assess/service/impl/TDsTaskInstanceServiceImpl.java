package com.atguigu.dga.assess.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.atguigu.dga.assess.mapper.TDsTaskInstanceMapper;
import com.atguigu.dga.assess.service.TDsTaskInstanceService;
import com.atguigu.dga.config.MetaConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-25
 */
@Service
public class TDsTaskInstanceServiceImpl extends ServiceImpl<TDsTaskInstanceMapper, TDsTaskInstance> implements TDsTaskInstanceService {

    //编写方法查询今天要考评的表的Task执行的元数据信息
    @Override
    public List<TDsTaskInstance> getAllTaskInstances(String assessDate, Set<String> names) {

        QueryWrapper<TDsTaskInstance> queryWrapper = new QueryWrapper<TDsTaskInstance>()
            //要求task的name必须是 库名.表名格式
            .in("name", names)
            //今天调度的日期和考评日期是同一天
            .eq("date(start_time)", assessDate)
            //任务一天可能会调度多次(出现了失败的情况)
            .eq("state", MetaConstant.TASK_STATE_SUCCESS);

        List<TDsTaskInstance> taskInstances = list(queryWrapper);

        //不包含sql语句，需要对查询到的数据处理，得到sql
        for (TDsTaskInstance taskInstance : taskInstances) {
            //脚本
            String rawScript = JSON.parseObject(taskInstance.getTaskParams()).getString("rawScript");
            //从脚本中抽取sql
            taskInstance.setSql(extractSqlFromScript(rawScript));
        }

        return taskInstances;
    }

    private String extractSqlFromScript(String rawScript) {
        /*
            明确截取的起始位置和终止位置。

            起始位置: 要么希望以with开头
              写法一:
               set xxx= xxx;
                with t1 as ();
                insert xxx
                select xxxx;
               set xxx= xxx;

              写法二: 没有with，获取insert的起始位置
             hql= "set xxx= xxx;
                insert xxx
                select xxxx "


              终止位置:  从起始位置往后找 结束符号。
                结束符号:  有;找;，没有找离开始向后最近的“
         */
        int start = rawScript.indexOf("with");

        //没有with，找insert的位置
        if (start == -1){
            start = rawScript.indexOf("insert");
        }

        int end = rawScript.indexOf(";", start);
        if (end == -1){
            end = rawScript.indexOf("\"", start);
        }

        return rawScript.substring(start,end);

    }
}
