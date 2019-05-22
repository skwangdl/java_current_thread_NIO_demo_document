package com.kepler.thread.pipedstream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class WriteData {

    public void writeMethod(PipedOutputStream out) throws IOException {
        System.out.println("write: ");
        for(int i = 0; i < 300; i ++){
            String outData = "" + (i + 1);
            out.write(outData.getBytes());
            System.out.println(outData);
        }
        System.out.println();
        out.close();
    }

}
