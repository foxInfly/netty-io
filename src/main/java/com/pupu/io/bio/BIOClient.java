package com.pupu.io.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**Socket --client
 * @author : lipu
 * @since : 2020-08-20 21:26
 */
public class BIOClient {



    public static void main(String[] args) throws Exception {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        for (int i = 0; i <2 ; i++) {
            Socket client = new Socket("localhost", 8080);
            OutputStream os = client.getOutputStream();

            String name = UUID.randomUUID().toString();
            String data = sf.format(new Date())+",客户端第"+i+"次发送数据："+name;
            System.out.println(data);

//            Thread.sleep(5000L);
            os.write(data.getBytes());
            os.close();
            client.close();

        }

        Thread.sleep(35000L);


    }

}
