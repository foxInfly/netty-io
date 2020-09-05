package com.pupu.io.netty.chat;

import com.pupu.io.netty.tomcat.GPTomcat1;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {
    private ChatClient connect(int port, String host, final String nickName) {

        //process flow  ，some thread
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("---------");
                            ch.pipeline().addLast(new GPTomcat1.GPTomcatHandler());
                        }
                    });
            //发起同步连接操作
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭，释放线程资源
            group.shutdownGracefully();
        }
        return this;
    }

    public static void main(String[] args) {
        new ChatClient().connect(8080, "localhost", "Tom 老师");
    }
}