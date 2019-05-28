package com.kepler.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestAsynFileChannel_B {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Path path = Paths.get("temp.txt");
        AsynchronousFileChannel asynchronousFileChannel =
                AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        System.out.println("lock begin: " + System.currentTimeMillis());
        Future<FileLock> future = asynchronousFileChannel.lock();
        System.out.println("lock end: " + System.currentTimeMillis());
        //A线程释放锁后，B线程才可以获得锁
        FileLock lock = future.get();
        System.out.println("B   get lock time = " + System.currentTimeMillis());
        lock.release();
        asynchronousFileChannel.close();
    }
}
