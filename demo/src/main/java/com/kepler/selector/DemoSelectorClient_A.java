package com.kepler.selector;

import java.io.IOException;
import java.net.Socket;

public class DemoSelectorClient_A {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        socket.getOutputStream().write("123456".getBytes());
        socket.close();
    }
}
