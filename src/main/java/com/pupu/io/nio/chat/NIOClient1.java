package com.pupu.io.nio.chat;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**Socket --client
 * @author : lipu
 * @since : 2020-08-20 21:26
 */
public class NIOClient1 {



    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 10; i++) {
            new NIOClientThread().start();
        }

    }

}
