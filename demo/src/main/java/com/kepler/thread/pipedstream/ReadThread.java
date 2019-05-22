package com.kepler.thread.pipedstream;

import java.io.IOException;
import java.io.PipedInputStream;

public class ReadThread extends Thread {
    private ReadData readData;
    private PipedInputStream pipedInputStream;

    public ReadThread(ReadData readData, PipedInputStream pipedInputStream) {
        this.readData = readData;
        this.pipedInputStream = pipedInputStream;
    }

    @Override
    public void run() {
        super.run();
        try {
            readData.readMethod(pipedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
