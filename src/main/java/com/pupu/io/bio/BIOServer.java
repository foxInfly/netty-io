package com.pupu.io.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**ServerSocket
 * @author : lipu
 * @since : 2020-08-20 21:27
 */
public class BIOServer {
    private int i =0;

    private ServerSocket server;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


     private BIOServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println(sf.format(new Date())+",BIO server start \nlisten port:" + port);
            System.out.println("===========================================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listen() throws Exception {
        while (true) {
            //here will be blocked,while client access
            Socket client = server.accept();
            System.out.println(sf.format(new Date())+",第"+(++i)+"次访问的客户端端口："+client.getPort());

            //对方给我发数据了
            InputStream is = client.getInputStream();
            System.out.println(sf.format(new Date())+",get inputStream from client");

            byte[] buff = new byte[1024];

            //here will be blocked,while the inputStream of client is not write date to zhe cache zone
            int len = is.read(buff);
            System.out.println(sf.format(new Date())+",read with inputStream from socket");

            if (len > 0) {
                String msg = new String(buff, 0, len);
                System.out.println(sf.format(new Date())+",收到：" + msg);
            }
            System.out.println("----------------------------------------------------");
            if(i<0){
                System.out.println("i:"+i);return;}
                Thread.sleep(1000);

        }
    }


    public static void main(String[] args) throws Exception {
        new BIOServer(8080).listen();
    }
}
