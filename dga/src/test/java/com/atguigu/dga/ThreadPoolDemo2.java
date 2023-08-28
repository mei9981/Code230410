package com.atguigu.dga;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Smexy on 2023/8/28
 *
 *  CompletableFuture: 1.8之后推出的更强大的线程任务类。
 *      Runable，Callable都没有这个类功能强大
 */
public class ThreadPoolDemo2
{
    public static void main(String[] args) {

        //创建一个内置了10个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        long start = System.currentTimeMillis();

        CompletableFuture<Integer> [] tasks = new CompletableFuture[10];
        //提交任务
        for (int i = 0; i < 10; i++) {
            //先线程池中借线程，向线程中跑任务
            int finalI = i;
            /*
                异步提交，速度快，不会阻塞
                返回的CompletableFuture就代表这个任务本身。
             */
            tasks[i] = CompletableFuture.supplyAsync(new Supplier<Integer>()
            {
                /*
                    类似Callable中的call

                 */
                @Override
                public Integer get() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(finalI+"号任务开始运行....");
                    return finalI;
                }
            },executorService);
        }

        /*
           等待刚刚提交的任务，全部运行结束。阻塞的！

            CompletableFuture.allOf(tasks): 要求对于集合中的每一个任务都要xxx
                xxx就是join(), join()是获取任务最重执行的结果。

                代表提交的10个任务，全部运行结果了，已经获得了结果
         */
        CompletableFuture.allOf(tasks).join();

        //遍历结果
        List<Integer> result = Arrays.stream(tasks)
                                      .map(future -> future.join())
                                      .collect(Collectors.toList());

        String str = result.stream().map(i -> i.toString()).collect(Collectors.joining(","));

        System.out.println("以下编号的任务已经完成，结果:"+str);

        //关闭池子
        executorService.shutdown();

        System.out.println("耗时:"+(System.currentTimeMillis() - start));

    }
}
