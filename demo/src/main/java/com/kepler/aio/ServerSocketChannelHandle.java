package com.kepler.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletionService;

/**
 * 这个处理器类，专门用来响应 ServerSocketChannel 的事件。
 * ServerSocketChannel只有一种事件：接受客户端的连接
 */
public class ServerSocketChannelHandle implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private AsynchronousServerSocketChannel serverSocketChannel;

    public ServerSocketChannelHandle(AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        this.serverSocketChannel.accept(attachment, this);
        ByteBuffer readBuffer = ByteBuffer.allocate(2550);

        //为这个新的socketChannel注册“read”事件，以便操作系统在收到数据并准备好后，主动通知应用程序
        //在这里，由于我们要将这个客户端多次传输的数据累加起来一起处理，所以我们将一个stringbuffer对象作为一个“附件”依附在这个channel上
        socketChannel.read(readBuffer, new StringBuffer(), new SocketChannelReadHandle(socketChannel , readBuffer));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("failed...");
    }
}
