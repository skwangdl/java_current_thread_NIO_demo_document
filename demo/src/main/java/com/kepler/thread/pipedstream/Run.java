package com.kepler.thread.pipedstream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Run {
    public static void main(String[] args) throws IOException, InterruptedException {
        WriteData writeData = new WriteData();
        ReadData readData = new ReadData();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream();
        pipedOutputStream.connect(pipedInputStream);
        ReadThread readThread = new ReadThread(readData, pipedInputStream);
        WriteThread writeThread = new WriteThread(writeData, pipedOutputStream);
        readThread.start();
        Thread.sleep(2000);
        writeThread.start();
    }
}
