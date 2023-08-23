package com.atguigu.dga.assess.assessor.spec;

import com.atguigu.dga.assess.assessor.AssessorTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Smexy on 2023/8/23
 */
@Component("AA")
@Deprecated
public class CheckAAssessor extends AssessorTemplate
{
    public void assess(){

        System.out.println("已经判断了是否有AA...");

    }
}
