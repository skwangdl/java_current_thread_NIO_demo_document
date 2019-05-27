package com.kepler.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class DemoSelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel_1 = ServerSocketChannel.open();
        channel_1.bind(new InetSocketAddress("localhost", 8888));
        channel_1.configureBlocking(false);
        //创建两个通道
        ServerSocketChannel channel_2 = ServerSocketChannel.open();
        channel_2.bind(new InetSocketAddress("localhost", 8889));
        channel_2.configureBlocking(false);
        Selector selector = Selector.open();
        //两个通道注册在选择器上
        channel_1.register(selector, SelectionKey.OP_ACCEPT);
        channel_2.register(selector, SelectionKey.OP_ACCEPT);
        boolean isRun = true;
            while (isRun == true){
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                channel.accept();
                InetSocketAddress ipAddress = (InetSocketAddress) channel.getLocalAddress();
                System.out.println(ipAddress.getPort() + " be connected");
            }
        }
        channel_1.close();
        channel_2.close();

    }
}
