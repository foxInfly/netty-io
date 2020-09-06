package com.pupu.io.nio.chat1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 网络多客户端聊天室
 * 功能1： 客户端通过Java NIO连接到服务端，支持多客户端的连接
 * 功能2：客户端初次连接时，服务端提示输入昵称，如果昵称已经有人使用，提示重新输入，如果昵称唯一，则登录成功，之后发送消息都需要按照规定格式带着昵称发送消息
 * 功能3：客户端登录后，发送已经设置好的欢迎信息和在线人数给客户端，并且通知其他客户端该客户端上线
 * 功能4：服务器收到已登录客户端输入内容，转发至其他登录客户端。
 */
public class NIOChatServer {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");     //
    //用来记录在线人数，以及昵称
    private static HashSet<String> users = new HashSet<String>();  //record the numer,nickname of online pepole
    
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILIT = "#@#";
    
    private Selector selector = null;
    
    public NIOChatServer(int port) throws IOException{
		
		this.port = port;
		//1.create ServerSocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(this.port));
        serverSocketChannel.configureBlocking(false);

        //2.create selector
		selector = Selector.open();

		//3.注册事件
        //     把serverSocketChannel为accept状态即建立连接的事件注册到selector，
        //      一点发生这个事件记录下来， selector.select()就会+1
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("服务已启动，监听端口是：" + this.port);
	}

    /*
    * 开始监听
    */
    public void listen() throws IOException{
    	while(true) {
    	    //如果有客户端连接进来，selector通过serverSocketChannel会发现，
            // 然后 serverSocketChannel.accept()会拿到对应的socketChannel,底层就是socket=流=一块内存
            int wait = selector.select();
            System.out.println(sf.format(new Date())+" selector.select()="+wait);
            if(wait == 0) continue; 
            Set<SelectionKey> keys = selector.selectedKeys();  //可以通过这个方法，知道可用通道的集合
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();

				process(key);
            }
        }
		
	}
    
    
    public void process(SelectionKey key) throws IOException {
        if(key.isAcceptable()){
        	ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);     //非阻塞模式
            //注册选择器，并设置为读取模式，就是给我监听client的读取操作，有的话告诉我
            // 收到一个连接请求，然后起一个SocketChannel，并注册到selector上，
            // 之后这个连接的数据，就由这个SocketChannel处理
            System.out.println("有客户端连接，IP地址为 :" + client.getRemoteAddress());
            client.register(selector, SelectionKey.OP_READ);
            
            //将此对应的channel（此处必是本都服务器的ServerSocketChannel）设置为准备接受其他客户端请求
            System.out.println("key.isAcceptable()="+key.isAcceptable());
            System.out.println("server.keyFor(selector).isAcceptable()="+server.keyFor(selector).isAcceptable());
            System.out.println("key.isReadable()="+key.isReadable());
            System.out.println("key.isWritable()="+key.isWritable());
            key.interestOps(SelectionKey.OP_ACCEPT);
            System.out.println("key.isAcceptable()="+key.isAcceptable());
//            System.out.println(sf.format(new Date())+""+key.toString());
            System.out.println("client.keyFor(selector).isAcceptable()="+client.keyFor(selector).isAcceptable());
            System.out.println("client.keyFor(selector).isReadable()="+client.keyFor(selector).isReadable());
            System.out.println("client.keyFor(selector).isWritable()="+client.keyFor(selector).isWritable());
            client.write(charset.encode("请输入你的昵称"));
            System.out.println("client.keyFor(selector).isAcceptable()="+client.keyFor(selector).isAcceptable());
            System.out.println("client.keyFor(selector).isReadable()="+client.keyFor(selector).isReadable());
            System.out.println("client.keyFor(selector).isWritable()="+client.keyFor(selector).isWritable());
        }
        //处理来自客户端的数据读取请求
        if(key.isReadable()){
            //返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel client = (SocketChannel)key.channel(); 
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try{
                while(client.read(buff) > 0) {
                    buff.flip();
                    content.append(charset.decode(buff));
                }
//                System.out.println("从IP地址为：" + sc.getRemoteAddress() + "的获取到消息: " + content);
                //将此对应的channel（此处必是客户端连接进来的channel）设置为准备下一次接受数据
                System.out.println("key.isAcceptable()="+key.isAcceptable());
                System.out.println("key.isReadable()="+key.isReadable());
                System.out.println("key.isWritable()="+key.isWritable());
                //这里设置为读，是为了，写出去时，对方接到后直接读
                key.interestOps(SelectionKey.OP_READ);
                System.out.println("key.isReadable()="+key.isReadable());
            }catch (IOException io){
            	key.cancel();
                if(key.channel() != null) {
                	key.channel().close();
                }
            }
            if(content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if(arrayContent != null && arrayContent.length == 1) {
                    String nickName = arrayContent[0];
                    if(users.contains(nickName)) {
                    	client.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int onlineCount = onlineCount();
                        String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                        broadCast(null, message);
                    }
                } 
                //注册完了，发送消息
                else if(arrayContent != null && arrayContent.length > 1) {
                    String nickName = arrayContent[0];
                    String message = content.substring(nickName.length() + USER_CONTENT_SPILIT.length());
                    message = nickName + " 说 " + message;
                    if(users.contains(nickName)) {
                        //不回发给发送此内容的客户端
                    	broadCast(client, message);
                    }
                }
            }
            
        }
    }

    public int onlineCount() {
        int res = 0;
        for(SelectionKey key : selector.keys()){
            Channel target = key.channel();
            
            if(target instanceof SocketChannel){
                res++;
            }
        }
        return res;
    }
    
    
    public void broadCast(SocketChannel client, String content) throws IOException {
        //广播数据到所有的注册到Selector的SocketChannel中
        System.out.println("广播："+selector.keys().size());
        for(SelectionKey key : selector.keys()) {
            Channel targetchannel = key.channel();
            //如果client不为空，不回发给发送此内容的客户端
            if(targetchannel instanceof SocketChannel && targetchannel != client) {
                SocketChannel target = (SocketChannel)targetchannel;
                target.write(charset.encode(content));
            }
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        new NIOChatServer(8080).listen();
    }
}
