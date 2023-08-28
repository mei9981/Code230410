package com.atguigu.dga.assess.assessor.quality;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.config.MetaConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smexy on 2023/8/28
 *
 *  必须日分区表
 *
 *  前一天(数据)产出的数据量，超过前x天平均产出量{upper_limit}% ，或低于{lower_limit}%  ，则给0分，其余10分
 */
@Component("TABLE_PRODUCT_VOLUME_MONITOR")
public class CheckTableVolumeAssessor extends AssessorTemplate
{
    @Value("${hdfs.admin}")
    private String admin;
    @Value("${hdfs.uri}")
    private String hdfsUri;
    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws URISyntaxException, Exception {

        String lifecycleType = param.getTableMetaInfo().getTableMetaInfoExtra().getLifecycleType();
        //判断是否是日分区表
        if (!MetaConstant.LIFECYCLE_TYPE_DAY.equals(lifecycleType)){
            return ;
        }
        //读取参数
        JSONObject params = JSON.parseObject(param.getMetric().getMetricParamsJson());
        Integer upperLimit = params.getInteger("upper_limit");
        Integer lowerLimit = params.getInteger("lower_limit");
        Integer days = params.getInteger("days");
        //有一个HDFS的客户端
        FileSystem hdfs = FileSystem.get(new URI(hdfsUri), new Configuration(), admin);
        //获取表路径
        String tableFsPath = param.getTableMetaInfo().getTableFsPath();
        //获取过去days天每一天当前表各个分区的产生量
        //String assessDate = param.getAssessDate();
        //TODO 为了测试
        String assessDate = "2022-06-11";
        List<PartitionSize> partitionSizes = statsPartitionDataSize(tableFsPath, hdfs, days, assessDate);

        //求最近一天分区的数据量
        String recentPartitionDt = LocalDate.parse(assessDate).minusDays(1).toString();
        PartitionSize recentPartitionSize = partitionSizes.get(0);

        //前x天平均产出量
        if (partitionSizes.size() <= 1){
            //只调度了一天
            return ;
        }
        double avgSize = partitionSizes.stream()
                                        .filter(p -> !p.getDt().equals(recentPartitionDt))
                                        .mapToLong(p -> p.size)
                                        .average()
                                        .getAsDouble();

        //对比
        BigDecimal upper = BigDecimal.valueOf(avgSize).multiply(BigDecimal.valueOf(100 + upperLimit)).movePointLeft(2);
        BigDecimal lower = BigDecimal.valueOf(avgSize).multiply(BigDecimal.valueOf(100 - lowerLimit)).movePointLeft(2);

        if (BigDecimal.valueOf(recentPartitionSize.size).compareTo(lower) == -1
            ||
            BigDecimal.valueOf(recentPartitionSize.size).compareTo(upper) == 1
        ){
            String msg = "dt=%s数据的产生量:%s,超过了过去%d的平均产出量%s,高于阈值%s%%,或者低于阈值%s%%";
            String str = String.format(msg, recentPartitionDt, recentPartitionSize.size,days,avgSize ,upperLimit, lowerLimit);
            assessScore(BigDecimal.ZERO,"最近1天数据产出量超过阈值",str,detail,false,null);
        }

    }

    private List<PartitionSize> statsPartitionDataSize(String tableFsPath, FileSystem hdfs, Integer days, String assessDate) throws IOException {

        List<PartitionSize> result = new ArrayList<>();
        for (Integer i = 0; i <= days; i++) {
            //求 assessDate - days - 1  ----- assessDate - 1 范围分区的数据产生量
            //求分区的日期
            String dt = LocalDate.parse(assessDate).minusDays(1).minusDays(i).toString();
            //获取分区的完整路径  强制要求日期类型的分区字段固定是dt
            Path dtPath = new Path(tableFsPath, "dt="+dt);
            //分区存在，再统计
            if (hdfs.exists(dtPath)){
                FileStatus[] dtFileStatus = hdfs.listStatus(dtPath);
                Long size = statSize(dtFileStatus, hdfs);
                result.add(new PartitionSize(dt,size));
            }
        }
        return result;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartitionSize{
        private String dt;
        private Long size;
    }

    private Long statSize(FileStatus[] dtFileStatus,FileSystem hdfs) throws IOException {
        Long size = 0l;
        for (FileStatus fileStatus : dtFileStatus) {

            if (fileStatus.isFile()){
                size += fileStatus.getLen();
            }else {
                FileStatus[] subFileStatus = hdfs.listStatus(fileStatus.getPath());
                statSize(subFileStatus,hdfs);
            }
        }
        return  size;
    }
}
