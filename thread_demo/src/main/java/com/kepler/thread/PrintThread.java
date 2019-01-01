package com.kepler.thread;

public class PrintThread extends Thread {

    private String message;

    public PrintThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("thread start, message: " + message);
    }
}
