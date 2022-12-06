package com.netty.javaproject.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * 客户端channel 同步、异步   发送消息
 */
public class TestChannel {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                //1. 连接到服务器
                //异步非阻塞，main 发起了调用，真正执行 connect 是nio线程
                //带有Future，Promise 的类型都是和 异步方法配套使用的。 用来处理结果
                .connect(new InetSocketAddress("localhost", 8080))


                /**
                 * 同步处理方法，主线程阻塞直到nio线程建立连接后，主线程再处理结果
                 */
//                .sync() //阻塞直到连接建立完毕，（因为是上面是异步的，这里不阻塞的话，下面获取的channel是没有建立连接的无效channel）
//                // 无阻塞向下执行获取 channel
//                .channel()
//                //2. 向服务器发送数据
//                .writeAndFlush("同步的 hello,word");


                /**
                 * 异步处理方法，在nio线程建立连接后，由 nio线程 继续执行 operationComplete 方法
                 */
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        Channel channel = channelFuture.channel();
                        System.out.println("当前线程：" + Thread.currentThread().getName());
                        channel.writeAndFlush("异步的 hello,word");
                    }
                });
        System.out.println("主线程执行完毕！");

    }
}
