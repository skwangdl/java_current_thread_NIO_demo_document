package com.kepler.current.phaser;

import java.util.concurrent.Phaser;

public class RootDemo {
    public static void main(String[] args){
        Phaser p1 = new Phaser(2);
        System.out.println("p1.getRegisteredParties()=" + p1.getRegisteredParties());
        Phaser p2 = new Phaser(p1, 2);
        System.out.println("p2.getRegisteredParties()=" + p2.getRegisteredParties());
        System.out.println("p1.getRegisteredParties()=" + p1.getRegisteredParties());
    }
}
