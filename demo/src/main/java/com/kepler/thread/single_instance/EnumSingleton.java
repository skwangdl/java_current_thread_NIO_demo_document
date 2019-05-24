package com.kepler.thread.single_instance;

public class EnumSingleton{

    public static void main(String[] args){
        Singleton obj1 = Singleton.INSTANCE;
        Singleton obj2 = Singleton.INSTANCE;
        System.out.println("obj1 == obj2? " + (obj1 == obj2));
    }

    private EnumSingleton(){}
    public static EnumSingleton getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    private static enum Singleton{
        INSTANCE;

        private EnumSingleton singleton;
        //JVM会保证此方法绝对只调用一次
        private Singleton(){
            singleton = new EnumSingleton();
        }
        public EnumSingleton getInstance(){
            return singleton;
        }
    }
}
