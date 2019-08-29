package com.ajin.test.demo;

/**
 * Created by Administrator on 2019/8/23.
 */
public class Test02 {
    static void pong(){
        System.out.println("pong");
    }

    public static void main(String[] args) {
        Thread t=new Thread(){
            public void run(){
                pong();
            }
        };
        t.start();
        System.out.println("ping");
    }
}
