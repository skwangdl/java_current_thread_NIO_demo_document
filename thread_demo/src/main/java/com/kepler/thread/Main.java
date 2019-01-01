package com.kepler.thread;

public class Main {
    public static void main(String[] args){
        PrintThread t = new PrintThread("hello kepler");
        t.start();
    }
}
