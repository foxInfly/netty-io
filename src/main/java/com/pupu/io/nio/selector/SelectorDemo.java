package com.pupu.io.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.IntBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**Selector
 * @author : lipu
 * @since : 2020-08-29 10:20
 */
public class SelectorDemo {


    /**注册事件
     * @author lipu
     * @since 2020/8/29 10:26
     */
    public Selector getSelector(Integer port) throws IOException {

        //创建Selector对象
        Selector sel = Selector.open();

        //创建可选择通道，并配置为非阻塞模式
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);

        //绑定通道到指定端口
        ServerSocket socket = server.socket();
        socket.bind(new InetSocketAddress(port));

        //向Selector中注册感兴趣的事
        server.register(sel, SelectionKey.OP_ACCEPT);

        return sel;


    }

    /**开始监听
     * @author lipu
     * @since 2020/8/29 10:26
     */
    public void listen(Integer port) throws IOException {
        Selector selector = getSelector(port);
        System.out.println("linten on " +port);
        while (true){
            //该调用会阻塞，直到至少有一个事件发生
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
//                process(key);
            }
        }



    }
}
