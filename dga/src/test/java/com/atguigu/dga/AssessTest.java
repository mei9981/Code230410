package com.atguigu.dga;

import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.mapper.TableMetaInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
}
