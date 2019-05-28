package com.txk.highchat.netty;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class HighChatServer {

    private static class SingletionHighChatServer {
        static final HighChatServer instance = new HighChatServer();
    }

    public static HighChatServer getInstance() {
        return SingletionHighChatServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    public HighChatServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new com.txk.highchat.netty.HighChatServerInitialzer());
    }

    public void start() {
        this.future = server.bind(8888);
        System.err.println("=================netty websocket server 启动完毕=============");
    }
}
