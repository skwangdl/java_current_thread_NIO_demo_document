package com.kepler.thread.pipedstream;

import java.io.IOException;
import java.io.PipedInputStream;

public class ReadData {

    public void readMethod(PipedInputStream input) throws IOException {
        System.out.println("read: ");
        byte[] byteArray = new byte[20];
        int readLength = input.read(byteArray);
        while (readLength != -1){
            String newData = new String(byteArray, 0, readLength);
            System.out.println(newData);
            readLength = input.read(byteArray);
        }
        System.out.println();
        input.close();
    }
}
