package com.kepler.thread.pipedstream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class WriteThread extends Thread {
    private WriteData writeData;
    private PipedOutputStream pipedOutputStream;

    public WriteThread(WriteData writeData, PipedOutputStream pipedOutputStream) {
        this.writeData = writeData;
        this.pipedOutputStream = pipedOutputStream;
    }

    @Override
    public void run() {
        try {
            writeData.writeMethod(pipedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
