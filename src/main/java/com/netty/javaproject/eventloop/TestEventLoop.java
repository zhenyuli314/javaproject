package com.netty.javaproject.eventloop;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TestEventLoop {
    public static void main(String[] args) {
        //1 创建事件循环组, 一个线程一个selector对应。 channel从始至终都绑定一个eventLoop，多个channel可以绑定到一个eventLoop
        NioEventLoopGroup group = new NioEventLoopGroup(2); //io事件， 普通任务， 定时任务
//        DefaultEventLoopGroup group = new DefaultEventLoopGroup(); //普通任务， 定时任务

        //2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //3. 执行普通任务(异步的)
        group.next().execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": abcdeg");
        });
        System.out.println(Thread.currentThread().getName());

        //4.定时任务
        group.next().scheduleAtFixedRate(()-> System.out.println("定时触发"), 0,1, TimeUnit.SECONDS);
    }
}
