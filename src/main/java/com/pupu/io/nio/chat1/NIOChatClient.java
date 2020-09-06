package com.pupu.io.nio.chat1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOChatClient {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final InetSocketAddress serverAdrress = new InetSocketAddress("localhost", 8080);
    private Selector selector = null;
    private SocketChannel client = null;
    
    private String nickName = "";
    private Charset charset = Charset.forName("UTF-8");
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPILIT = "#@#";
    
    
    public NIOChatClient() throws IOException{

        //1.create a SocketChannel,这里连接上，默认是走到Accept状态
        client = SocketChannel.open(serverAdrress);     //连接远程主机的IP和端口
        System.out.println(sf.format(new Date())+""+client);
        client.configureBlocking(false);

        //2.crerate a selector
        selector = Selector.open();
        //3.regist SocketChannel to selector,同时设置为读状态，就是对面来数据直接可以读了
        client.register(selector, SelectionKey.OP_READ);
    }
    
    public void session(){
        new Reader().start();//开辟一个新线程从服务器端读数据
        new Writer().start();//开辟一个新线程往服务器端写数据
	}

    /**
     * 在主线程中 从键盘读取数据输入到服务器端
     */
    private class Writer extends Thread{
		@Override
		public void run() {
			try{
		        Scanner scan = new Scanner(System.in);
		        while(scan.hasNextLine()){
		            String line = scan.nextLine();
		            if("".equals(line)) continue; //不允许发空消息
		            if("".equals(nickName)) {
		            	nickName = line;
		                line = nickName + USER_CONTENT_SPILIT;
		            } else {
		                line = nickName + USER_CONTENT_SPILIT + line;
		            }
//		            client.register(selector, SelectionKey.OP_WRITE);
		            client.write(charset.encode(line));//client既能写也能读，这边是写
		        }
		        scan.close();
			}catch(Exception e){
				
			}
		}
    	
    }

    /**
     *从服务器端读数据
     */
    private class Reader extends Thread {
        public void run() {
            try {
                while(true) {
//                    int readyChannels = selector.select();
//                    if(readyChannels == 0) continue;
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();  //可以通过这个方法，知道可用通道的集合
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                    while(keyIterator.hasNext()) {
                         SelectionKey key = keyIterator.next();
                         keyIterator.remove();
                         process(key);
                    }
                }
            }
            catch (IOException io){
            	
            }
        }

        private void process(SelectionKey key) throws IOException {
            System.out.println(sf.format(new Date())+""+key.channel().toString());
            System.out.println("key.isAcceptable()="+key.isAcceptable());
            System.out.println("key.isReadable()="+key.isReadable());
            System.out.println("key.isWritable()="+key.isWritable());
            if(key.isReadable()){
                //使用 NIOServerDemoBak 读取 Channel中的数据，这个和全局变量client是一样的，因为只注册了一个SocketChannel
                //client既能写也能读，这边是读
                SocketChannel sc = (SocketChannel)key.channel();
                
                ByteBuffer buff = ByteBuffer.allocate(1024);
                String content = "";
                while(sc.read(buff) > 0)
                {
                    buff.flip();
                    content += charset.decode(buff);
                }
                //若系统发送通知名字已经存在，则需要换个昵称
                if(USER_EXIST.equals(content)) {
                	nickName = "";
                }
                System.out.println(content);


                //更改本地的
                key.interestOps(SelectionKey.OP_READ);
                System.out.println("key.isReadable()="+key.isReadable());
            }
        }
    }
    
    
    
    public static void main(String[] args) throws IOException {
        new NIOChatClient().session();
    }
}
