package com.atguigu.dga;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import com.atguigu.dga.assess.bean.TDsTaskInstance;
import com.atguigu.dga.assess.service.GovernanceAssessDetailService;
import com.atguigu.dga.assess.service.TDsTaskInstanceService;
import com.atguigu.dga.config.MetaConstant;
import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.mapper.TableMetaInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Smexy on 2023/8/23
 */
@SpringBootTest
public class AssessTest
{
    @Autowired
    private TableMetaInfoMapper mapper;
    @Test
    void testQueryMetaInfo(){
        List<TableMetaInfo> res = mapper.queryTableMetaInfo("gmall", "2023-08-22");
        System.out.println(res);

    };

    /*
        BigDecimal:  2 / 3 * 10

         RoundingMode.HALF_UP: 四舍五入
     */
    @Test
    void BigDecimalCal(){

        BigDecimal res = BigDecimal.valueOf(2)
                                          .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP)
                                          //右侧移动小数点，乘以10
                                          .movePointRight(1);
        //.multiply(BigDecimal.TEN)
        System.out.println(res);


    }

    /*
          正则表达式: https://www.runoob.com/regexp/regexp-tutorial.html
            ^： 开头
            $:  结尾
            |:  或
            \w:  匹配任意一个 a-z,A-z,_ 字符
             .:  匹配任意一个\n,\r外的字符
            \d:  匹配任意一个数字0-9
            +: 代表匹配多次

            \: 在java中是特殊字符，如果希望编写\，需要转义。 \\ 才是 \
     */
    @Test
    void testRegexPattern(){

        //String s1 = "ods_log_inc";
        //String s1 = "ods_activity_info_full";
        String s1 = "ods_activity_info_full_ahhaha";

        //正则表达式的内容
        String regex = "^ods_\\w+_(inc|full)$";
        //把这个内容变为一个正则表达式对象
        Pattern pattern = Pattern.compile(regex);
        //使用对象去验证字符串
        Matcher matcher = pattern.matcher(s1);
        //判断是否匹配
        if (matcher.matches()){
            System.out.println(s1 + " 符合ods层的规则!");
        }
    }

    @Autowired
    private GovernanceAssessDetailService detailService;

    @Autowired
    private ApplicationContext context;
    @Test
    void testAssess(){

        //AssessorTemplate assessor = context.getBean("HAVE_TABLE_COMMENT",AssessorTemplate.class);
        detailService.assess("gmall","2023-05-26");

    }

    /*
        set集合: [a,b,c]
        set集合: [b,c,d]
            两个集合有多少个元素重复？ 取交集
     */
    @Test
    void testSetInter(){

        Set<String> set1 = Sets.newSet("a", "b", "c");
        Set<String> set2 = Sets.newSet("d", "b", "c");

        System.out.println("集合运算前:"+set1);
        //集合取交集
        boolean ifInter = set1.retainAll(set2);
        /*
            如果两个集合产生了交集，此时 ifInter 为true，
            交集的结果是存在前一个集合中,会删除前一个集合和后一个集合相差的部分。
         */
        System.out.println("集合运算后:"+set1);
        if (ifInter){
            System.out.println("两个集合交集的结果:" + set1);
        }

    }

    @Value("${hdfs.admin}")
    private String admin;
    @Value("${hdfs.uri}")
    private String hdfsUri;
    /*
        获取指定目录的权限，转换为三位数
     */
    @Test
    void testGetPermission() throws Exception {

        FileSystem hdfs = FileSystem.get(new URI(hdfsUri), new Configuration(), admin);

        String file = "hdfs://hadoop102:8020/warehouse/gmall/ods/ods_activity_info_full";

        FileStatus fileStatus = hdfs.getFileStatus(new Path(file));

        FsPermission permission = fileStatus.getPermission();
        /*
            按时是-，转换为0，不是-，就转为1
           ownner  group  other
            rwx    r-x    r-x
            111    101    101
            7      5      5
         */
        System.out.println(permission);
        //转换为3位数格式

        //获取owner的权限，自动转换为数字
        String permissionStr = "" + permission.getUserAction().ordinal() + permission.getGroupAction().ordinal() + permission.getOtherAction().ordinal();

        System.out.println(permissionStr);


    }

    @Autowired
    private TDsTaskInstanceService taskInstanceService;
    /*
        如果只查询N列，这列也不在bean中，不想额外创建bean。可以使用Map封装。
     */
    @Test
    void testSelectMap(){

        QueryWrapper<TDsTaskInstance> queryWrapper = new QueryWrapper<TDsTaskInstance>()
            .eq("date(start_time)", "2023-05-26")
            .eq("state", MetaConstant.TASK_STATE_SUCCESS)
            .eq("name", "gmall.dim_sku_full")
            .select("timestampdiff(second ,start_time,end_time) sec", "name");

        /*
                只有一行： 调用getMap()，返回一个Map
                        一行，都封装为一个Map，key是列名，value是列值。

                结果有N行: 调用 listMaps()，返回值是List<Map>
                        一行，都封装为一个Map，key是列名，value是列值。
                        把多个Map封装到一个List中
         */
        Map<String, Object> result = taskInstanceService.getMap(queryWrapper);

        //{sec=65, name=gmall.dim_sku_full}
        System.out.println(result);

    }

    /*
        很多语言中，可以使用
            %s，作为一个String类型的占位符
            %d,作为一个数值类型的占位符

            %需要转义，%%代表一个%
     */
    @Test
    void testString(){

        String msg = "dt=%s数据的产生量:%s,超过了阈值%s%%,或者低于阈值%s%%";
        String str = String.format(msg, "2023-08-23", 20, 30, 40);
        System.out.println(str);

    }


}
