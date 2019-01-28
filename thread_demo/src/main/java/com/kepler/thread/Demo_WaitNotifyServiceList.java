package com.kepler.thread;

import java.util.ArrayList;
import java.util.List;

public class Demo_WaitNotifyServiceList {
    volatile private List list = new ArrayList();

    public void add(){
        list.add("anyString");
    }

    public int size(){
        return list.size();
    }
}
