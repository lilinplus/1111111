package com.baidu.call.controller;

public class Son extends Father{
    public static void main(String[] args) {
        new Son().protectedMethod();
        System.out.println(new Son().age);
    }
    protected void protectedMethod() {
        super.protectedMethod();
        System.out.println("自己的保护方法");
    }
}
