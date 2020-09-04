package com.pupu.io.nio.chat;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Socket --client
 *
 * @author : lipu
 * @since : 2020-08-20 21:26
 */
public class NIOClientThread extends Thread {


    @SneakyThrows
    @Override
    public void run() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Socket client = new Socket("localhost", 8080);
        OutputStream os = client.getOutputStream();
        InputStream is = client.getInputStream();

        String name = UUID.randomUUID().toString();
        String data = Thread.currentThread().getName() + ":" + sf.format(new Date()) + ",客户端发送数据：" + name;
        System.out.println(data);

//            Thread.sleep(5000L);
        os.write(data.getBytes());

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            String str = new String(bytes, 0, len);
            System.out.println(Thread.currentThread().getName() + "  " + str);
        }
        System.out.println("====test");
        Thread.sleep(6000L);
        is.close();

        os.close();
        client.close();


    }


}
