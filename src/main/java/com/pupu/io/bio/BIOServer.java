package com.pupu.io.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : lipu
 * @since : 2020-08-20 21:27
 */
public class BIOServer {

    ServerSocket server;


    public BIOServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("BIO服务已启动，监听的端口是：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void listen() throws IOException {
        while (true) {
            Socket client = server.accept();
            System.out.println("客户端端口："+client.getPort());

            //对方给我发数据了
            InputStream is = client.getInputStream();


            byte[] buff = new byte[1024];

            int len = is.read(buff);

            if (len > 0) {
                String msg = new String(buff, 0, len);
                System.out.println("收到：" + msg);
            }



        }
    }


    public static void main(String[] args) throws IOException {
        new BIOServer(8080).listen();
    }
}
