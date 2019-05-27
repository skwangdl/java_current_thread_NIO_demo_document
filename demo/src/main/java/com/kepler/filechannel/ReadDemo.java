package com.kepler.filechannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadDemo {
    private static FileInputStream fisRef;
    private static FileChannel fileChannel;

    public static void main(String[] args) throws IOException {
        fisRef = new FileInputStream(new File("temp.txt"));
        fileChannel = fisRef.getChannel();
        fileChannel.position(2);
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.position(3);
        //将通道内的字符读入缓冲区内，方法具有同步性
        fileChannel.read(byteBuffer);
        byte[] getByteArray = byteBuffer.array();

        for(int i = 0; i < getByteArray.length; i ++){
            if(getByteArray[i] == 0){
                System.out.println("space");
            }else{
                System.out.println((char)getByteArray[i]);
            }
        }
        fileChannel.close();
        fisRef.close();
    }
}
