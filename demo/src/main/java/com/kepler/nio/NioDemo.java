package com.kepler.nio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class NioDemo {
//    Buffer 都是非线程安全
//    Buffer缓冲区4个核心技术点，容量capacity, 限制limit, 位置position, 标记mark，这4个技术点之间的值的大小关系：
//          0 <= 标记mark <= 位置position <= 限制limit <= 容量capacity
//    (1)缓冲区的capacity， limit, position, mark都不能为负数
//    (2)如果定义了mark, 则在将position或limit调整为小于该mark标记的值时，该mark将被丢弃
//    (3)如果未定义mark, 那么调用reset()方法将导致抛出InvalidMarkException异常
//    (4)如果position大于新的limit, 则position的值就是新的limit值
//    (5)limit和position的值一样时，当在指定的position写入数据时会出现异常
    public static void main(String[] args) throws UnsupportedEncodingException {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        System.out.println(charBuffer.capacity());
        System.out.println(charBuffer.limit(6));
        System.out.println(charBuffer.limit());
        System.out.println(charBuffer.position(3));
        System.out.println(charBuffer.position());
        System.out.println(charBuffer.mark());

        byte[] byteArray = new byte[]{1,2,3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.position(1);
        byteBuffer.mark();
        System.out.println("bytebuffer在" + byteBuffer.position() + "位置设置mark标记");
        byteBuffer.position(2);
        byteBuffer.reset();
        System.out.println("bytebuffer回到" + byteBuffer.position() + "位置");

//        clear()还原缓冲区到初始的状态，包含将limit设置为0，将limit设置为capacity，并丢弃mark，作用是一切为默认
//        byteBuffer.clear();
//        reset()重置为以前mark的位置，调用此方法不更改也不丢弃mark的值
//        byteBuffer.reset();
//        flip()反转此缓冲区，首先将limit设置为当前位置，然后将position设置为0，如果已定义了mark，则丢弃该mark
//        缩小可操作limit缓冲区的范围
//        byteBuffer.flip();
//        rewind()重绕此缓冲区，将position设置为0并丢弃mark,在“重新写入或获取”的操作之前调用此方法，mark清除，位置position归0，limit不变
//        byteBuffer.rewind();
//        compact()压缩此缓冲区，将缓冲区的当前位置和界限之间的字节复制到缓冲区的开始处
//        byteBuffer.compact();

        //创建直接缓冲区，JVM会尽量的在直接缓冲区上执行本机IO操作，直接对内核空间进行访问，比较快一些
        ByteBuffer buffer1 = ByteBuffer.allocateDirect(100);
        System.out.println(buffer1.isDirect());
        //创建非直接缓冲区,在JVM内创建的缓冲区
        ByteBuffer buffer2 = ByteBuffer.allocate(100);
        System.out.println(buffer1.isDirect());

        //byteBuffer转换CharBuffer,设定编码格式
        byte[] byteArray1 = "中国人".getBytes("utf-8");
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(byteArray1);
        CharBuffer charBuffer1 = Charset.forName("utf-8").decode(byteBuffer1);
        for(int i = 0; i < charBuffer1.limit(); i ++){
            System.out.println(charBuffer1.get() + "");
        }
    }
}
