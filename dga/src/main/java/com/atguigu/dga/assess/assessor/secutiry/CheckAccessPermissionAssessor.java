package com.atguigu.dga.assess.assessor.secutiry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.AssessParam;
import com.atguigu.dga.assess.bean.GovernanceAssessDetail;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smexy on 2023/8/25
 *
 *  检查该表最高权限的目录或者文件，
 *      需要遍历表整个目录，找到最高权限的目录或文件。
 *          需要有HDFS客户端，还需要使用递归。
 *
 *
 *      如果超过文件超过{file_permission}或者目录超过{dir_permission}则给0分 其余给10分。
 *          {"file_permission":644,"dir_permission":755}
 *
 */
@Component("PERMISSION_CHECK")
public class CheckAccessPermissionAssessor extends AssessorTemplate
{
    @Value("${hdfs.admin}")
    private String admin;
    @Value("${hdfs.uri}")
    private String hdfsUri;

    @Override
    protected void assess(AssessParam param, GovernanceAssessDetail detail) throws Exception {
        //读取权限的极限阈值参数
        JSONObject paramJO = JSON.parseObject(param.getMetric().getMetricParamsJson());
        Integer filePermission = paramJO.getInteger("file_permission");
        Integer dirPermission = paramJO.getInteger("dir_permission");
        //有一个HDFS的客户端
        FileSystem hdfs = FileSystem.get(new URI(hdfsUri), new Configuration(), admin);
        //遍历表的目录
        TableMetaInfo tableMetaInfo = param.getTableMetaInfo();
        //找到当前表在hdfs的路径
        String tableFsPath = tableMetaInfo.getTableFsPath();

        FileStatus fileStatus = hdfs.getFileStatus(new Path(tableFsPath));

        //编写方法，递归当前表目录，检测权限是否超过阈值
        //准备返回值
        List<JSONObject> result = new ArrayList<>();
        checkPermission(result,fileStatus,hdfs,filePermission,dirPermission);

        if (!result.isEmpty()){
            assessScore(BigDecimal.ZERO,"目录中有文件或目录权限超过阈值",JSON.toJSONString(result),detail,false,null);
        }
    }

    private void checkPermission(List<JSONObject> result,FileStatus fileStatus, FileSystem hdfs, Integer filePermission, Integer dirPermission) throws IOException {


        if (fileStatus.isDirectory()){
            //先检查这个目录的权限是否超过阈值
            JSONObject jsonObject = comparePermission(fileStatus, dirPermission);
            if (jsonObject != null){
                result.add(jsonObject);
            }
            //递归遍历这个目录，为目录的每个文件都检查是否超过阈值
            FileStatus[] subFileStatus = hdfs.listStatus(fileStatus.getPath());
            for (FileStatus status : subFileStatus) {
                checkPermission(result,status,hdfs,filePermission,dirPermission);
            }
        }else{
            //只检查这个文件是否超过阈值
            JSONObject jsonObject = comparePermission(fileStatus, filePermission);
            if (jsonObject != null){
                result.add(jsonObject);
            }
        }
    }

    //编写方法，比对当前这个文件的权限是否超过阈值，如果超过了，将其记录
    private JSONObject comparePermission(FileStatus fileStatus,Integer limit){
        //获取当前文件的权限，需要转换为 三位数格式
        FsPermission permission = fileStatus.getPermission();
        Integer permissionInt = Integer.valueOf("" + permission.getUserAction().ordinal() + permission.getGroupAction().ordinal() + permission.getOtherAction().ordinal());
        //判断当前文件的类型，是目录还是文件
        if (permissionInt > limit){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileName",fileStatus.getPath().toString());
            jsonObject.put("permisson",permissionInt);
            jsonObject.put("msg","当前权限: "+permissionInt + "  超过了权限阈值:"+ limit);
            return jsonObject;
        }
        //没有超过阈值
        return null;
    }
}
