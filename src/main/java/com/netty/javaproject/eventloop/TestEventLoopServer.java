package com.netty.javaproject.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class TestEventLoopServer {
    public static void main(String[] args) {
        //细分2：创建一个独立的 EventLoopGroup
        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
        new ServerBootstrap()
                //boss  和 worker
                //细分1：boss 只负责 ServerSocketChannel 上 accept 事件， worker只负责 socketChannel 上的读写事件
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                nioSocketChannel.pipeline().addLast(new StringDecoder());
                                nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(Thread.currentThread().getName()+":"+msg);
                                        ctx.fireChannelRead(msg); //让消息传递给下个 handler
                                    }
                                }).addLast(defaultEventLoopGroup, "handlerName", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(Thread.currentThread().getName()+":"+msg);
                                        ctx.fireChannelRead(msg);
                                    }
                                });
                            }
                        }
                )
                .bind(8080);
    }
}
