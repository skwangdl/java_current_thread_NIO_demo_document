package com.kepler.thread;

import org.junit.Test;

public class Demo_ThreadSortRun {

    @Test
    public void run(){
        Demo_ThreadSort t1 = new Demo_ThreadSort(1);
        Demo_ThreadSort t2 = new Demo_ThreadSort(2);
        Demo_ThreadSort t3 = new Demo_ThreadSort(3);
        Demo_ThreadSort t4 = new Demo_ThreadSort(4);
        Demo_ThreadSort t5 = new Demo_ThreadSort(5);
        Demo_ThreadSort t6 = new Demo_ThreadSort(6);
        Demo_ThreadSort t7 = new Demo_ThreadSort(7);
        Demo_ThreadSort t8 = new Demo_ThreadSort(8);
        Demo_ThreadSort t9 = new Demo_ThreadSort(9);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
    }
}
