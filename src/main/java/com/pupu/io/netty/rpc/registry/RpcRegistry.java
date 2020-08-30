package com.pupu.io.netty.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author : lipu
 * @since : 2020-08-30 22:42
 */
public class RpcRegistry {

    private int port;
    public RpcRegistry(int port){
        this.port=port;
    }


    public void start(){
        //基于NIO来实现，Selector主线程池，Work线程（子线程）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        try {
            server.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //在netty中，把所有的业务处理全部归总到了一个队列中
                            //这个队列中包含了各种各样的处理逻辑，对这些处理逻辑在netty中有一个封装
                            //封装成了一个对象，无锁化串行任务队列
                            //Pipline
                            ChannelPipeline pipeline = ch.pipeline();

                            //就是对我们处理逻辑的封装
                            //对于自己定义协议的内容进行编解码
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            //自定义编码器
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //实参处理
                            pipeline.addLast("encode",new ObjectEncoder());
                            pipeline.addLast("decode",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            //前面的编解码，就是完成对数据的解析
                            //最后一步，执行属于自己的逻辑
                            //1、注册，就是每一个对象起一个名字（对外提供服务的额名字）
                            //2、服务的位置要做一个等级
                            pipeline.addLast(new RegistryHandler());

                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            //正式启动服务，相当于用一个死循环开始轮询
            ChannelFuture future = server.bind(this.port).sync();
            System.out.println("GP RPC Registry start listen at "+this.port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();

    }
}
