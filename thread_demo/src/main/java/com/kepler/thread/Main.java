package com.kepler.thread;

public class Main extends Thread {
    private String message;

    public static void main(String[] args){
        Main t = new Main("hello kepler");
        t.start();
    };

    public Main(String message){
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("thread start, message: " + message);
    }
}
