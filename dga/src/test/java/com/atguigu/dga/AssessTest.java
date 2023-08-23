package com.atguigu.dga;

import com.atguigu.dga.meta.bean.TableMetaInfo;
import com.atguigu.dga.meta.mapper.TableMetaInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
}
