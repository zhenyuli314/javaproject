package com.netty.javaproject.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyFutureTest {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        Future<String> future = eventLoop.submit(() -> {
            log.debug("执行计算");
            Thread.sleep(1000);
            return "123";
        });
        //1.同步法
        log.debug("同步接受结果：{}", future.getNow()); //getNow不会阻塞，直接返回 null
        //2.异步法
        future.addListener((future1) -> { //当结果返回回来才会触发事件，所以这里也算是阻塞了一下
            log.debug("异步接受结果：{}", future1.getNow()); //getNow不会阻塞，直接返回 null
        });
    }
}
