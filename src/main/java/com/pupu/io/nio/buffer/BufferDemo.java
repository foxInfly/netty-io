package com.pupu.io.nio.buffer;

import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**验证 position、limit 和 capacity 这几个值的变化过程
 * @author : lipu
 * @since : 2020-08-28 20:06
 */
public class BufferDemo {
    public static void main(String[] args) throws Exception{

        //创建文件IO流
        FileInputStream fin = new FileInputStream("E://test.txt");
        //常见文件的操作管道
        FileChannel fc = fin.getChannel();

        //创建一个10大小的缓冲区，说白了就是分配一个10个大小的byte数组。
        ByteBuffer buffer = ByteBuffer.allocate(10);
        output("初始化缓冲区",buffer);

        //把管道内的内容（不能大于buffer的容量）读到缓冲区
        fc.read(buffer);
        output("调用FileChannel.read(buffer)，把管道内的数据读到缓冲区",buffer);

        //准备操作之前，先锁定操作范围:limit=position,position=0
        buffer.flip();
        output("调用flip()，重置limit=position,position=0",buffer);

        //判断有没有可读数据
        while (buffer.remaining()>0){
            byte b = buffer.get();
//            System.out.println((char) b);
            output("调用get(),一次读取一个字节Byte",buffer);
        }

        //可以理解为解锁,把所有值的状态变化为初始化时的状态
        buffer.clear();
        output("调用clear()，把所有值的状态变化为初始化时的状态",buffer);

        //关闭流
        fin.close();


    }

    //把这个缓冲区里面实时状态给打印出来
    public static void output(String step, Buffer buffer){
        System.out.println(step+" : ");
        //容量，数组大小
        System.out.print("capacity: "+ buffer.capacity()+", ");
        //当前操作数据所在的位置，也可以叫做游标
        System.out.print("position: "+ buffer.position()+", ");
        //锁定值。flip，数据操作范围索引只能在position至limit之间
        System.out.println("limit:" + buffer.limit());
        System.out.println();
    }
}
