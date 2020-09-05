package com.pupu.io.netty.tomcat;

import com.pupu.io.netty.tomcat.http.GPRequest;
import com.pupu.io.netty.tomcat.http.GPResponse;
import com.pupu.io.netty.tomcat.http.GPServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**netty版本Tomcat
 * netty就是一个同时支持多协议的网络通信框架
 * @author : lipu
 * @since : 2020-08-29 23:35
 */
public class GPTomcat1 {
    private static Map<String, GPServlet> servletMapping  = new HashMap<>();

    public static void main(String[] args) {

        //1.加载web.xml文件，同时初始化 servletMapping对象
        Properties webxml = new Properties();
        try {
            String WEB_INF = GPTomcat1.class.getResource("/").getPath();//get root classpath
//            String WEB_INF = GPTomcat1.class.getClassLoader().getResource("").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");
            webxml.load(fis);

            for (Object k : webxml.keySet()) {
                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webxml.getProperty(key);
                    String className = webxml.getProperty(servletName + ".className");
                    //单实例，多线程
                    GPServlet obj = (GPServlet) Class.forName(className).newInstance();
                    servletMapping.put(url,obj);
                }
            }
            System.out.println("初始化的ServletMapping： "+servletMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //2.创建对象
        //Boss线程(池)
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Worker线程(池)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //Netty服务
            //ServerBootstrap-->ServerSocketChannel-->newChannel, create Channel Object
            ServerBootstrap server = new ServerBootstrap();

            //3.配置参数
            //链路式编程
            server.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class) //主线程处理类，看到这样的写法，底层就是用反射,最终clazz.newInstance()
                    .childHandler(new ChannelInitializer<SocketChannel>() {//子线程处理类，Handler
                        //客户端初始化处理
                        @Override
                        protected void initChannel(SocketChannel client){
                            //无锁化串行编程，Netty对HTTP协议的封装，顺序有要求
                            client.pipeline().addLast(new HttpResponseEncoder());//HttpResponseEncoder 编码器
                            client.pipeline().addLast(new HttpRequestDecoder());//HttpRequestDencoder 解码器
                            client.pipeline().addLast(new GPTomcatHandler()); //业务逻辑处理
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)//针对主线程的配置 分配线程最大数量128
                    .childOption(ChannelOption.SO_KEEPALIVE,true); //针对子线程的配置  保持长连接

            //4.启动服务器
            ChannelFuture f = server.bind(8080).sync();

            System.out.println("GP Tomcat 已启动，坚挺的端口是： "+8080);

            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static class GPTomcatHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;

                //转交给我们自己的request实现...
                GPRequest request =  new GPRequest(ctx,  req);
                //转交给我们自己的response实现...
                GPResponse response =  new GPResponse(ctx, req);
                //实际业务处理
                String url = request.getUrl();

                if (servletMapping.containsKey(url)) {
                    servletMapping.get(url).service(request,response);
                }else {
                    response.write("404 - Not Found: "+url);
                }

            }
        }
    }



}
