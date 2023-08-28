package com.atguigu.dga.assess.service.impl;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.assessor.spec.CheckBusiOwnnerAssessor;
import com.atguigu.dga.assess.assessor.spec.CheckTecOwnnerAssessor;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.assess.bean.GovernanceMetric;
import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.atguigu.dga.assess.mapper.GovernanceAssessDetailMapper;
import com.atguigu.dga.assess.service.GovernanceAssessDetailService;
import com.atguigu.dga.assess.service.GovernanceMetricService;
import com.atguigu.dga.assess.service.TDsTaskInstanceService;
import com.atguigu.dga.config.MetaInfoUtil;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.service.TableMetaInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * <p>
 * 治理考评结果明细 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-23
 */
@Service
public class GovernanceAssessDetailServiceImpl extends ServiceImpl<GovernanceAssessDetailMapper, GovernanceAssessDetail> implements GovernanceAssessDetailService {

    @Autowired
    private TableMetaInfoService metaInfoService;

    @Autowired
    private GovernanceMetricService metricService;

    @Autowired
    private MetaInfoUtil metaInfoUtil;

    @Autowired
    private TDsTaskInstanceService taskInstanceService;
    @Autowired
    private ApplicationContext context;

    //注入线程池对象
    @Autowired
    private ThreadPoolTaskExecutor executorService;
    /*
        1.考生进考场
                考生: 数仓中的表的元数据信息
                查询今天要参与考评的表的元数据信息

              从table_meta_info 和 table_meta_info_extra查询今天要参与测评的所有表的元数据信息。

        2.发试卷考试
                试卷:  测评指标  GovernanceAssessDetail
                考题:  一个测评指标  GovernanceMetric

                每一张表，都要考评17个指标

                assessor: 阅卷老师，判断题应该得多少分

        3.收集结果，押运，判分
                把每一张表考试的结果，存入数据库。
                List<GovernanceAssessDetail>
     */
    @Override
    public void assess(String db,String assessDate) {

        //重复执行了，希望先删除，再写入
        remove(new QueryWrapper<GovernanceAssessDetail>().eq("schema_name",db).eq("assess_date",assessDate));

        //待考评的所有表的元数据
        List<TableMetaInfo> tableMetaInfos = metaInfoService.queryTableMetaInfo(db, assessDate);

        //清空Map，保证每次查询到的信息都是最新的
        metaInfoUtil.tableMetaInfoMap.clear();
        //把所有表的元数据信息，封装为Map<String,TableMetaInfo>,方便使用表名获取对应的元数据信息
        for (TableMetaInfo tableMetaInfo : tableMetaInfos) {
            String key = tableMetaInfo.getSchemaName() + "." + tableMetaInfo.getTableName();
            metaInfoUtil.tableMetaInfoMap.put(key,tableMetaInfo);
        }

        //查询所有表的Task执行的元数据(包含sql)
        List<TDsTaskInstance> allTaskInstances = taskInstanceService.getAllTaskInstances(assessDate, metaInfoUtil.tableMetaInfoMap.keySet());
        for (TDsTaskInstance taskInstance : allTaskInstances) {
            metaInfoUtil.taskInstancesMap.put(taskInstance.getName(),taskInstance);
        }


        //查询今天要考评的指标
        List<GovernanceMetric> metrics = metricService.list(
            new QueryWrapper<GovernanceMetric>()
                .eq("is_disabled", "否")
        );

        /*
            任务集合
                每个任务运行一张表的全部指标的考评过程。 返回 List<GovernanceAssessDetail>
         */
        CompletableFuture<List<GovernanceAssessDetail>>[] tasks = new CompletableFuture[tableMetaInfos.size()];
        //提交任务
        for (int i = 0; i < tasks.length; i++) {
            /*
                异步提交，速度快，不会阻塞
                返回的CompletableFuture就代表这个任务本身。
             */
            int finalI = i;
            tasks[i] = CompletableFuture.supplyAsync(new Supplier<List<GovernanceAssessDetail>>()
            {
                @Override
                public List<GovernanceAssessDetail> get() {
                    //运行考评
                    List<GovernanceAssessDetail> details = runAssess(tableMetaInfos.get(finalI), metrics, assessDate);
                    return details;
                }
            },executorService);
        }
        // 等待刚刚提交的任务，全部运行结束。阻塞的！
        CompletableFuture.allOf(tasks).join();

        /*
            遍历结果
            输入: 每张表都是一个 [GovernanceAssessDetail,GovernanceAssessDetail ]
                    [
                      [GovernanceAssessDetail,GovernanceAssessDetail ],
                      [GovernanceAssessDetail,GovernanceAssessDetail ]
                      .....
                    ]

            结果: [ GovernanceAssessDetail,GovernanceAssessDetail ]
         */
        List<GovernanceAssessDetail> result = Arrays.stream(tasks)
                                                     .flatMap(task -> task.join().stream())
                                                     .collect(Collectors.toList());

        //遍历
        /*for (TableMetaInfo tableMetaInfo : tableMetaInfos) {
            for (GovernanceMetric metric : metrics) {
                *//*
                    完成考试逻辑
                    判断当前这道题是什么题，把这个题交给对应的批卷老师(assessor)
                 *//*
                *//*if (metric.getMetricCode().equals("HAVE_TEC_OWNER")){
                    new CheckTecOwnnerAssessor().assess();
                }else if (metric.getMetricCode().equals("HAVE_BUS_OWNER")){
                    new CheckBusiOwnnerAssessor().assess();
                }*//*
                *//*
                    ... 17个if判断.能解决问题，维护性差。
                     违背了软件开发的一个原则：
                        开闭原则：
                            开:  增加新功能时，不能去修改之前的代码。
                            闭:  删除旧功能时，不能去修改之前的代码。

                      设计模式:
                            开发软件时，设计功能时，常常使用的一些固定的套路。
                            套路针对某些特定的场景设计的，可以解决一些特定的问题。

                          模版模式。可以解决这个问题。
                 *//*

                //封装考评参数
                AssessParam param = new AssessParam(tableMetaInfo, metric, assessDate);
                //使用模版父类对象，来执行方法。 为父类对象提供子类实现。
                AssessorTemplate assessor = context.getBean(metric.getMetricCode(),AssessorTemplate.class);
                //进行考评
                GovernanceAssessDetail detail = assessor.doAssess(param);
                result.add(detail);
            }
        }*/

        //存入数据库
        saveBatch(result);

    }

    private List<GovernanceAssessDetail> runAssess(TableMetaInfo tableMetaInfo, List<GovernanceMetric> metrics, String assessDate) {
        List<GovernanceAssessDetail> result = new ArrayList<>();

        for (GovernanceMetric metric : metrics) {
            //封装考评参数
            AssessParam param = new AssessParam(tableMetaInfo, metric, assessDate);
            //使用模版父类对象，来执行方法。 为父类对象提供子类实现。
            AssessorTemplate assessor = context.getBean(metric.getMetricCode(), AssessorTemplate.class);
            //进行考评
            GovernanceAssessDetail detail = assessor.doAssess(param);
            result.add(detail);

        }
        return result;
    }
}
