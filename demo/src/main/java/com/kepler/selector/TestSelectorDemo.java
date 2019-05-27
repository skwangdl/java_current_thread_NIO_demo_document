package com.kepler.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class TestSelectorDemo {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //必须将ServerSocketChannel设置成非阻塞的，否则抛出IllegalBlockingModeException
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress("localhost", 8888));
        //通道注册在选择器中
        //相同的通道可以注册到不同的选择器，返回的SelectionKey不是同一个对象
        //只有相同的通道注册在同一个选择器，才会返回同一个SelectionKey对象
        Selector selector = Selector.open();
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("selector=" + selector);
        System.out.println("key=" + key);
        serverSocket.close();
        serverSocketChannel.close();
    }
}
