package com.pupu.io.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**NIO的操作过于繁琐，才有了Netty
 *     就是对这一系列的繁琐操作进行封装
 * @author : lipu
 * @since : 2020-08-20 22:25
 */
public class NIOServerDemo {
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int port = 8080;
    //准备轮询器selctor，缓存buff
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    //初始化Selector
    public NIOServerDemo(int port) {
        //初始化通道
        try {
            this.port = port;
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(this.port));
            server.configureBlocking(false);//关闭阻塞

            //开启轮询器
            selector = Selector.open();


            //把轮询器注册到ServerSocketChannel
            server.register(selector, SelectionKey.OP_ACCEPT);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void listen() {
        System.out.println(sf.format(new Date())+",listen on " + this.port + ".");

        //轮询主线程
        try {
            while (true) {
                //这里会阻塞，至少有一个值
                System.out.println(sf.format(new Date())+",开始轮询");
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                System.out.println(sf.format(new Date())+",keys1 " + keys.size());
                Iterator<SelectionKey> iter = keys.iterator();

                //同步体现在这里，因为一次只能拿一个key，处理一种状态
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();
                    System.out.println(sf.format(new Date())+",keys2 " + keys.size());
                    //每一个key代表一种状态，对应一个业务
                    process(key);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理业务
    private void process(SelectionKey key) throws Exception{
        //针对每一种key给出一个反应
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //这个方法体现非阻塞，不管你数据有没有准备好，你给我一个状态和反馈
            SocketChannel channel = server.accept();
            System.out.println(sf.format(new Date())+",RemoteAddress1："+ channel.getRemoteAddress());
            channel.configureBlocking(false);
            //当数据准备就绪的时候，更改状态为read,可读
            key = channel.register(selector,SelectionKey.OP_READ);

        }else if (key.isReadable()) {
            //key.channel(),从多路复用器中拿到客户端的引用
            SocketChannel channel = (SocketChannel) key.channel();
            System.out.println(sf.format(new Date())+",RemoteAddress2："+ channel.getRemoteAddress());
            int len = channel.read(buffer);
            if (len >0) {
                buffer.flip();
                String content = new String(buffer.array(),0,len);
                key = channel.register(selector,SelectionKey.OP_WRITE);
                //在key上携带一个附件，一会写出去
                key.attach("我是服务器");
                System.out.println(sf.format(new Date())+",读取内容："+content);
            }
        }else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            System.out.println(sf.format(new Date())+",RemoteAddress3："+ channel.getRemoteAddress());
            String content = (String) key.attachment();
            Thread.sleep(1000);
            channel.write(ByteBuffer.wrap((sf.format(new Date())+",输出："+content).getBytes()));
            System.out.println("写出完毕------------------------------------------");
            channel.close();
        }
    }


    public static void main(String[] args) {
        new NIOServerDemo(8080).listen();
    }
}
