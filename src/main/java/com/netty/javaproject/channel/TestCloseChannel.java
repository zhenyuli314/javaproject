package com.netty.javaproject.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * 客户端channel关闭后 同步、异步的附加操作
 */
public class TestCloseChannel {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler());
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));
        Channel channel = channelFuture.sync().channel();
        new Thread(
                ()->{
                    Scanner scanner = new Scanner(System.in);
                    while (true){
                        String line = scanner.nextLine();
                        if ("q".equals(line)){
                            channel.close();
                            System.out.println("（放到这里是无效的）关闭之后的操作。。。"); //这样是无效的
                            break;
                        }
                        channel.writeAndFlush(line);
                    }
                }
        ).start();

        /**
         * 同步法
         */
        ChannelFuture closeFuture = channel.closeFuture();
//        closeFuture.sync();
//        System.out.println("同步法的，关闭之后的操作。当前线程："+Thread.currentThread().getName());

        /**
         * 异步法
         */
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("异步法的，关闭之后的操作。当前线程："+Thread.currentThread().getName());
                group.shutdownGracefully(); //关闭selector相关的其他线程。执行此步之后，eventLoop不在接受新的任务，把当前的任务执行完之后就会释放所有资源。server那边同理
            }
        });
    }
}
