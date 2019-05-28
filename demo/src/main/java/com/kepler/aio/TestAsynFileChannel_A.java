package com.kepler.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestAsynFileChannel_A {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Path path = Paths.get("temp.txt");
        AsynchronousFileChannel asynchronousFileChannel =
                AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        //设定锁定通道的起始与结束为止，如果shared为false，在释放锁之前，其他线程不可以读写该通道
        Future<FileLock> future = asynchronousFileChannel.lock(0,3,false);
        FileLock fileLock = future.get();
        System.out.println("A  get lock time = " + System.currentTimeMillis());
        Thread.sleep(5000);
        //A线程释放锁后，B线程才可以获得锁
        fileLock.release();
        System.out.println("A  release lock time = " + System.currentTimeMillis());
        asynchronousFileChannel.close();
    }
}
