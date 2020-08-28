package com.pupu.io.nio.buffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : lipu
 * @since : 2020-08-28 21:34
 */
public class DirectBuffer {

    public static void main(String[] args) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        FileInputStream fin = new FileInputStream("E://test.txt");
        FileChannel fcin = fin.getChannel();

        FileOutputStream fout = new FileOutputStream(String.format("F:\\project\\IDEA\\study\\javaee57\\netty-io\\src\\test\\test.txt"));
        FileChannel fcout = fout.getChannel();

        while (true){
            buffer.clear();
            int r = fcin.read(buffer);
            if (r == -1) {
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
    }
}
