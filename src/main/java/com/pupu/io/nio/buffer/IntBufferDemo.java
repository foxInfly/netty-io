package com.pupu.io.nio.buffer;

import java.nio.IntBuffer;

/**
 * @author : lipu
 * @since : 2020-08-27 23:54
 */
public class IntBufferDemo {

    public static void main(String[] args) {

        //分配新的int缓冲区，参数为缓冲区的容量
        //新缓冲区的当前位置将为零，其界限（限制位置）将为其容量。它将具有一个底层实现数组，其数组偏移量将为零。
        IntBuffer buffer = IntBuffer.allocate(8);

        for (int i = 0; i < buffer.capacity(); i++) {
            int j = 2 * (i + 1);
            //将给定整数写入此缓冲区的当前位置。当前位递增
            buffer.put(j);
        }
        //重设此缓冲区（指针，就是数组的下标），将限制设置为当前位置，然后将挡位置设置为0
        buffer.flip();
        //查看在当前位置和限制位置之间是否有元素
        while (buffer.hasRemaining()){
            //读取此缓冲区当前位置的整数，然后当前位置递增
            int j = buffer.get();
            System.out.print(j + " ");
        }

    }
}
