package com.pupu.io.nio.buffer;

import java.nio.ByteBuffer;

/**缓冲区分片
 * @author : lipu
 * @since : 2020-08-28 21:15
 */
public class BufferSlice {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);

        //给缓冲区赋值，0-9
        for (int i = 0; i <buffer.capacity() ; i++) {
            buffer.put((byte) i);
        }

        //创建子缓冲区
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();

        //改变子缓冲区的内容
        for (int i = 0; i < slice.capacity(); i++) {
            slice.put(i, (byte) (slice.get(i)*10));
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.remaining()>0){
            System.out.println(buffer.get());
        }
    }
}
