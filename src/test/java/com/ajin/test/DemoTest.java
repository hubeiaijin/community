package com.ajin.test;

/**
 * Created by Administrator on 2019/8/23.
 */
public class DemoTest {
    public static void main(String[] args) {
        new children("mike");
    }
}
class people{
    String name;
    public  people(){
        System.out.println(1);
    }
    public people(String name){
        System.out.println(2);
        this.name=name;
    }
}
class children extends people{
    people father;
    public children(String name){
        System.out.println(3);
        this.name=name;
        father=new people(name+":F");
    }
    public children(){
        System.out.println(4);
    }
}