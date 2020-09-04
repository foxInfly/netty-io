package com.pupu.io.nio.chat;

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

/**
 * NIO的操作过于繁琐，才有了Netty
 * 就是对这一系列的繁琐操作进行封装
 *
 * @author : lipu
 * @since : 2020-08-20 22:25
 */
public class NIOServerDemo1 {

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //1.create a ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);//close block


        //2.create a Selector
        Selector selector = Selector.open();

        //3.register the ServerSocketChannel to special Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        System.out.println(sf.format(new Date()) + ",listen on 8080.");

        while (true) {
            System.out.println(sf.format(new Date()) + ",开始轮询");
            selector.select(); //这里会阻塞，至少有一个值

            Set<SelectionKey> keys = selector.selectedKeys();
            System.out.println(sf.format(new Date()) + ",keys1 " + keys.size());
            Iterator<SelectionKey> iter = keys.iterator();

            //同步体现在这里，因为一次只能拿一个key，处理一种状态
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                System.out.println(sf.format(new Date()) + ",selector.selectedKeys().size() " +selector.selectedKeys().size());
                //每一个key代表一种状态，对应一个业务;针对每一种key给出一个反应
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    //这个方法体现非阻塞(不会阻塞)，直接向下走
                    SocketChannel channel = server.accept();
                    System.out.println(sf.format(new Date()) + ",RemoteAddress1：" + channel.getRemoteAddress());
                    channel.configureBlocking(false);
                    //当数据准备就绪的时候，更改状态为read,可读
                    channel.register(selector, SelectionKey.OP_READ);
                    Thread.sleep(1000);
                    System.out.println("更改客户端channel状态为Read");
                    System.out.println(sf.format(new Date()) + ",selector.selectedKeys().size() " + selector.selectedKeys().size());

                } else if (key.isReadable()) {
                    //key.channel(),从多路复用器中拿到客户端的引用
                    SocketChannel channel = (SocketChannel) key.channel();
                    System.out.println(sf.format(new Date()) + ",RemoteAddress2：" + channel.getRemoteAddress());
                    int len = channel.read(buffer);
                    if (len > 0) {
                        buffer.flip();
                        String content = new String(buffer.array(), 0, len);
                        Thread.sleep(1000);
                        key = channel.register(selector, SelectionKey.OP_WRITE);
                        //1在key上携带一个附件，一会写出去
                        key.attach("我是服务器.");
                        System.out.println(sf.format(new Date()) + ",读取内容：" + content);
                    }
                    System.out.println(sf.format(new Date()) + ",selector.selectedKeys().size() " + selector.selectedKeys().size());
                } else if (key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    System.out.println(sf.format(new Date()) + ",RemoteAddress3：" + channel.getRemoteAddress());
                    String content = (String) key.attachment();
                    Thread.sleep(1000);
                    channel.write(ByteBuffer.wrap((sf.format(new Date()) + ",输出：" + content).getBytes()));
                    System.out.println("写出完毕.------------------------------------------");
                    channel.close();
                }
            }


        }
    }
}
