package com.kepler.internet;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Test {
    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
        while (networkInterface.hasMoreElements()){
            NetworkInterface eachNetworkInterface = networkInterface.nextElement();
            System.out.println("getName获得网络设备名称=" + eachNetworkInterface.getName());
            System.out.println("getDisplayName获得网络设备显示名称=" + eachNetworkInterface.getDisplayName());
            System.out.println("getIndex获得网络接口的索引=" + eachNetworkInterface.getIndex());
            System.out.println("isUp是否已经开启并运行=" + eachNetworkInterface.isUp());
            System.out.println("isLoopback是否为回调接口=" + eachNetworkInterface.isLoopback());
            System.out.println();
        }
    }
}
