package com.atguigu.dga.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.assess.service.GovernanceAssessDetailService;
import com.atguigu.dga.score.bean.GovernanceAssessGlobal;
import com.atguigu.dga.score.bean.GovernanceAssessTecOwner;
import com.atguigu.dga.score.service.CalScoreService;
import com.atguigu.dga.score.service.GovernanceAssessGlobalService;
import com.atguigu.dga.score.service.GovernanceAssessTecOwnerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Smexy on 2023/8/28
 */
@RestController
@RequestMapping("/governance")
public class ScoreController
{

    @Autowired
    private GovernanceAssessGlobalService globalService;

    public String getMaxAssessDate(){
        GovernanceAssessGlobal governanceAssessGlobal = globalService.getOne(new QueryWrapper<GovernanceAssessGlobal>().select("max(assess_date) assess_date "));
        return governanceAssessGlobal.getAssessDate();
    }
    /*
        {  "assessDate":"2023-04-01" ,"sumScore":90, "scoreList":[20,40,34,55,66] }
            规范，存储，计算，质量，安全
     */
    @GetMapping("/globalScore")
    public Object getGlobalScore(){
        //计算最近的考评日期
        String maxAssessDate = getMaxAssessDate();
        GovernanceAssessGlobal governanceAssessGlobal = globalService.getOne(new QueryWrapper<GovernanceAssessGlobal>().eq("assess_date",maxAssessDate));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("assessDate",maxAssessDate);
        jsonObject.put("sumScore",governanceAssessGlobal.getScore());
        jsonObject.put("scoreList", Arrays.asList(
            governanceAssessGlobal.getScoreSpec(),
            governanceAssessGlobal.getScoreStorage(),
            governanceAssessGlobal.getScoreCalc(),
            governanceAssessGlobal.getScoreQuality(),
            governanceAssessGlobal.getScoreSecurity()
        ));

        return jsonObject;
    }

    @Autowired
    private GovernanceAssessDetailService detailService;

    @GetMapping("/problemList/{governType}/{pageNo}/{pageSize}")
    public Object getProblemList(@PathVariable("governType") String type, @PathVariable("pageNo") Integer pageNo,
                                 @PathVariable("pageSize") Integer pageSize){
        //计算起始数据的index
        int from = (pageNo - 1) * pageSize;
        //计算最近的考评日期
        String maxAssessDate = getMaxAssessDate();
        QueryWrapper<GovernanceAssessDetail> queryWrapper = new QueryWrapper<GovernanceAssessDetail>()
            .eq("assess_date", maxAssessDate)
            .eq("governance_type", type)
            .lt("assess_score",10)
            .orderByAsc("id")
            .last(" limit " + from + "," + pageSize);

        List<GovernanceAssessDetail> list = detailService.list(queryWrapper);

        return list;
    }

    /*
        {"SPEC":1, "STORAGE":4,"CALC":12,"QUALITY":34,"SECURITY":12}
     */
    @GetMapping("/problemNum")
    public Object getProblemNum(){
        //计算最近的考评日期
        String maxAssessDate = getMaxAssessDate();
        QueryWrapper<GovernanceAssessDetail> queryWrapper = new QueryWrapper<GovernanceAssessDetail>()
            .eq("assess_date", maxAssessDate)
            .lt("assess_score",10)
            .select("governance_type","count(*) num")
            .groupBy("governance_type");


        /*
            [
                {"governance_type":"CALC","num":20},
                {"governance_type":"SPEC","num":20},
                ...
            ]

         */
        List<Map<String, Object>> maps = detailService.listMaps(queryWrapper);

        JSONObject jsonObject = new JSONObject();
        for (Map<String, Object> map : maps) {
            jsonObject.put((String)map.get("governance_type"),map.get("num"));
        }

        return jsonObject;
    }

    @Autowired
    private GovernanceAssessTecOwnerService tecOwnerService;
    /*
        [{"tecOwner":"zhang3" ,"score":99},
{"tecOwner":"li4" ,"score":98},
{"tecOwner": "wang5","score":97}   ]
     */
    @GetMapping("/rankList")
    public Object getTecOwnnerRank(){
        //计算最近的考评日期
        String maxAssessDate = getMaxAssessDate();

        QueryWrapper<GovernanceAssessTecOwner> queryWrapper = new QueryWrapper<GovernanceAssessTecOwner>()
            .eq("assess_date", maxAssessDate)
            .select("tec_owner tecOwner","score")
            .orderByDesc("score")
            ;

        List<Map<String, Object>> maps = tecOwnerService.listMaps(queryWrapper);

        return maps;
    }

    @Value("${assess.dbs}")
    private String dbs;

    @Autowired
    private CalScoreService calScoreService;
    @PostMapping("/assess/{date}")
    public Object reCalScore(@PathVariable("date")String date) throws Exception {

        String[] dbNames = dbs.split(",");

        for (String dbName : dbNames) {
            //考评
            detailService.assess(dbName,date);
            //重新计算得分
            calScoreService.calScore(date);
        }

        return "success";
    }
}
