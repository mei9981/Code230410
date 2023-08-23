package com.atguigu.dga.assess.assessor.spec;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Smexy on 2023/8/23
 */
@Component("HAVE_BUS_OWNER")
public class CheckBusiOwnnerAssessor extends AssessorTemplate
{
    public void assess(){

        System.out.println("已经判断了是否有业务负责人...");

    }
}
