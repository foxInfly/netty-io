package com.pupu.io.bio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @author : lipu
 * @since : 2020-08-20 21:26
 */
public class BIOClient {



    public static void main(String[] args) throws IOException {

        int count = 100;

        Socket client = new Socket("localhost", 8080);
        OutputStream os = client.getOutputStream();

        String name = UUID.randomUUID().toString();
        System.out.println("客户端发送数据："+name);

        os.write(name.getBytes());

        os.close();
        client.close();

    }

}
