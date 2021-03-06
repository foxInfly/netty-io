package com.pupu.io.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**内存映射
 * @author : lipu
 * @since : 2020-08-28 21:47
 */
public class MappedBuffer {
    
    private static final int start = 0;
    private static final int size = 1024;
    
    
    public static void main(String[] args) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("E://test.txt", "rw");
        FileChannel fc = raf.getChannel();

        //把缓冲区跟文件系统进行一个映射关联
        //只要操作缓冲区里面的内容，文件内容也会跟着改变
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);

        raf.close();
    }
}
