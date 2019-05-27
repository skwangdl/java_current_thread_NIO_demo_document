package com.kepler.filechannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadLargeFileDemo {
    private static RandomAccessFile fisRef;
    private static FileChannel fileChannel;
    private static ByteBuffer byteBuffer = ByteBuffer.allocate((int)(1024 * 1024 * 1024));

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        fisRef = new RandomAccessFile(new File("E:\\mysql-installer-community-8.0.15.0.msi"), "rw");
        fileChannel = fisRef.getChannel();

        Thread thread1 = new Thread(){
            @Override
            public void run() {
                try {
                    fileChannel.read(byteBuffer, 0);
                    System.out.println(" end thread 1 " + System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                try {
                    fileChannel.write(ByteBuffer.wrap("1111".getBytes()), fileChannel.size() + 1);
                    System.out.println(" end thread 2 " + System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("  begin time: " + System.currentTimeMillis());
        thread1.start();
        Thread.sleep(100);
        thread2.start();
    }
}
