package com.kepler.filechannel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileLockDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("temp.txt", "rw");
        FileChannel fileChannel = file.getChannel();
        System.out.println("lock before " + System.currentTimeMillis());
//        文件锁，配置开始与结束的锁定位置，如果shared为false，则只能自身线程读写，否则其他线程也可以读写
        fileChannel.lock(0,2,false);
        System.out.println("lock after " + System.currentTimeMillis());
        file.close();
        fileChannel.close();
    }
}
