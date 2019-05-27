package com.kepler.selector;

import java.io.IOException;
import java.net.Socket;

public class DemoSelectorClient_B {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8889);
        socket.close();
    }
}
