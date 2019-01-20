package com.kepler.thread;

public class Demo_ControllerThreadNoSafe {
    private static String usernameRef;
    private static String passwordRef;

    synchronized public static void doPost(String username, String password) throws InterruptedException {
        usernameRef = username;
        passwordRef = password;
        System.out.println("username= " + usernameRef + " password= " + passwordRef);
    }
}
