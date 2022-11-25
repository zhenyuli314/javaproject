package com.netty.javaproject.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        //1. 启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2. BossEventLoop，WorkerEventLoop（seletor，thread），group组
                .group(new NioEventLoopGroup())
                //3. 选择服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                //4. boss 负责处理连接，worker（child） 负责处理读写，决定了 worker（child）能执行哪些操作（handler）
                .childHandler(
                        //5. channel 代表和客户端进行数据读写的通道 Initializer 初始化，负责添加别的 handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            //14. 和客户端建立连接后，调用初始化方法
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                // 添加具体的 handler
                                nioSocketChannel.pipeline().addLast(new StringDecoder()); //将ByteBuf 转为字符串的 handler
                                nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){ //自定义的 handler
                                    @Override // 读事件
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                        ctx.fireChannelRead(msg); //将读事件发给下一个 handler
                                    }
                                });
                            }
                        }
                )
                //6. 绑定指定端口
                .bind(8080);

    }
}
