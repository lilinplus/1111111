package com.baidu.call.controller;

public class Father {
    private String name;
    protected String sex;
    public final String age = "18";

    public void publicMethod() {
        System.out.println("公有方法");
    }

    protected void protectedMethod() {
        System.out.println("保护方法");
    }

    private void privateMethod() {
        System.out.println("私有方法");
    }
}
