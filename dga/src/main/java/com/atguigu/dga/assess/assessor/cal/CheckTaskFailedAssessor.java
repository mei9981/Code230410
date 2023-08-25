package com.atguigu.dga.assess.assessor.cal;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.atguigu.dga.assess.service.TDsTaskInstanceService;
import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Smexy on 2023/8/25
 *
 * 检查DS 当天调度中是否有报错。有则给0分，否则给10分.
 *
 *   ds调度的日期，和考评日期是一天！
 *
 *  检测ds生成的TaskInstance的运行记录，判断是否报错。
 *      查看t_ds_task_instance表中数据的state列，
 *          6: 失败
 *          7： 成功
 *
 *      规范： 要求数仓的开发人员在调度任务时，每一个Task的命名必须以 库名.表名作为命名！
 *      t_ds_task_instance表中数据的name列，找到对应表的导数的task的运行情况。
 */
@Component("TASK_FAILED")
public class CheckTaskFailedAssessor extends AssessorTemplate
{
    @Autowired
    private TDsTaskInstanceService taskInstanceService;
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws URISyntaxException, Exception {

        //生成instance的name
        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        String instanceName = tableMetaInfo.getSchemaName() + "." + tableMetaInfo.getTableName();
        //有些表是不运行sql来调度，例如ods层的表(一个脚本 load), dim_date(一年只手动导入一次)
        if (MetaConstant.DW_LEVEL_ODS.equals(tableMetaInfo.getTableMetaInfoExtra().getDwLevel()) ||
            "dim_date".equals(tableMetaInfo.getTableName())
        ){
            return ;
        }
        //根据实例的名字，查询元数据，判断实例在调度时是否报错
        QueryWrapper<TDsTaskInstance> queryWrapper = new QueryWrapper<TDsTaskInstance>()
            .eq("date(start_time)", param.getAssessDate())
            .eq("name", instanceName)
            .eq("state", 6);

        List<TDsTaskInstance> list = taskInstanceService.list(queryWrapper);

        if (!list.isEmpty()){
            String faildMsg = list.stream().map(i -> i.getId() + "-" + i.getName() + "-" + i.getStartTime() + "-" + i.getEndTime()).collect(Collectors.joining(","));
            assessScore(BigDecimal.ZERO,instanceName +" 任务实例运行出现了失败",faildMsg,detail,false,null);
        }
    }
}
