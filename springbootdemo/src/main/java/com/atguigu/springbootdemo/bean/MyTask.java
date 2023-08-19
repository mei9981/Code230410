package com.atguigu.springbootdemo.bean;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by Smexy on 2023/8/19
 *
 *  定时调度：
 *          crontab: linux。功能简单
 *          DS： 不算轻量级
 *          SpringBoot： 轻量级
 */
@Component
public class MyTask
{
    //计数
    private int num;

    /*
        @Scheduled 标识当前方法是定时调度的。
        调度方式：3选1
            cron： 使用crontab表达式来执行调度.
            fixedDelay： 上一次执行结束后，延迟n个时间，再执行下一次
            fixedRate：  上一次执行开始后，间隔n个时间，就执行下一次。
                            如果上一次执行，未完成，会阻塞。
        其他配置:
            zone: 指定时区
            initialDelay： 第一次执行时，可以延迟n个时间
            timeUnit： 时间单位


     */
    //@Scheduled(cron = "*/5 * * * * *")
    //@Scheduled(zone = "Asia/Shanghai",timeUnit = TimeUnit.SECONDS,initialDelay = 5,fixedDelay = 3)
    @Scheduled(zone = "Asia/Shanghai",timeUnit = TimeUnit.SECONDS,initialDelay = 5,fixedRate = 3)
    public void myTask() throws InterruptedException {
        System.out.println(++num + " 启动time: " +LocalDateTime.now());
        Thread.sleep(4000);
        System.out.println(num + " 结束time: " +LocalDateTime.now());
    }
}
