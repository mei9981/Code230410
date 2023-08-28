package com.atguigu.dga;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Smexy on 2023/8/28
 *
 *  比如现在需要运行很多个线程，但是每个线程执行的时长都很短。
 *      需要话费大量的时间去启动线程，和销毁线程，造成资源和时间的浪费。
 *
 *      解决： 提前准备好一个池子，这个池子中已经准备好了创建好的线程。
 *              线程使用完毕后，放入池子，不销毁，之后再提供给其他方法用。
 */
public class ThreadPoolDemo
{
    public static void main(String[] args) {

        //创建一个内置了10个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //提交线程中运行的任务
        MyThread3 myThread3 = new MyThread3();
        executorService.submit(myThread3);
        executorService.submit(myThread3);
        executorService.submit(myThread3);

        //关闭池子
        executorService.shutdown();

    }
}
