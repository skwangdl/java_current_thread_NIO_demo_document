package com.kepler.filechannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        FileOutputStream fosRef = new FileOutputStream(new File("temp.txt"));
        FileChannel fileChannel = fosRef.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap("kepler".getBytes());
        System.out.println("A fileChannel.position()=" + fileChannel.position());
        System.out.println("write() 1 返回值： " + fileChannel.write(buffer));
        System.out.println("B fileChannel.position()=" + fileChannel.position());
        fileChannel.position(2);
//        write()方法具有同步性，线程安全
        System.out.println("write() 2 返回值: " + fileChannel.write(buffer));
        System.out.println("C fileChannel.position()=" + fileChannel.position());
        fileChannel.close();
        fosRef.close();
    }
}
