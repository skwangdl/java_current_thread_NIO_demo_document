package com.kepler.selector;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class DemoSelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel_1 = ServerSocketChannel.open();
        channel_1.bind(new InetSocketAddress("localhost", 8888));
        channel_1.configureBlocking(false);
        //创建通道
        Selector selector = Selector.open();
        //两个通道注册在选择器上
        SelectionKey selectionKey1 = channel_1.register(selector, SelectionKey.OP_ACCEPT);
        boolean isRun = true;
        while (isRun == true){
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    ServerSocket serverSocket = channel.socket();
                    Socket socket = serverSocket.accept();
                    InetSocketAddress ipAddress = (InetSocketAddress) channel.getLocalAddress();
                    System.out.println(ipAddress.getPort() + " be connected");

                    InputStream inputStream = socket.getInputStream();
                    byte[] byteArray = new byte[1024];
                    int readLength = inputStream.read(byteArray);

                    while (readLength != -1){
                        String newString = new String(byteArray, 0, readLength);
                        System.out.println(newString);
                        readLength = inputStream.read(byteArray);
                    }
                    inputStream.close();
                    socket.close();
                    iterator.remove();
                }
            }
        }
        channel_1.close();
    }
}
