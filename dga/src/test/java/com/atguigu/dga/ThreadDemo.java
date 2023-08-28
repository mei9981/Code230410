package com.atguigu.dga;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Smexy on 2023/8/28
 *   线程的使用方式
 *  1.自己编写类，继承Thread类
 *
 *  2.java是单继承，因此extends 位置很珍贵。
 *      不想浪费这个位置，
 *      实现 Runnable（jdk 1.0）
 *
 *  3. jdk1.5，推出了更强大的实现
 *      Callable
 *          优势: ①可以在运行线程的逻辑时，抛出异常
 *               ②可以提供返回值
 *
 */
public class ThreadDemo
{
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //第一种
        MyThread1 myThread1 = new MyThread1();
        myThread1.start();

        //第二种
        MyThread2 myThread2 = new MyThread2();
        Thread thread = new Thread(myThread2);
        thread.start();

        //第三种
        MyThread3 myThread3 = new MyThread3();
        //一个可以返回线程运行结果的一个Task(任务)
        FutureTask<String> futureTask = new FutureTask<>(myThread3);
        Thread thread1 = new Thread(futureTask);
        thread1.start();

        //获取返回值 会阻塞当前线程
        String res = futureTask.get();
        System.out.println(res);

        System.out.println("haha");

    }
}

class MyThread1 extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"运行了...");
    }
}

class MyThread2 implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"运行了...");
    }
}

class MyThread3 implements Callable<String>
{
    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName()+"运行了...");
        return "ok";
    }
}
