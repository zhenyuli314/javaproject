package com.netty.javaproject.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //7. 启动类
        new Bootstrap()
                //8. 添加 EventLoop
                .group(new NioEventLoopGroup())
                //9. 选择客户端 channel 实现
                .channel(NioSocketChannel.class)
                //10. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 13. 在连接建立后被调用
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                //11. 连接到服务器
                .connect(new InetSocketAddress("localhost", 8080))
                //12. 阻塞直到连接建立
                .sync()
                .channel()
                //15.向服务器发送数据
                .writeAndFlush("hello,word");
                //16.调用pipeline将string转为ByteBuf
    }
}
