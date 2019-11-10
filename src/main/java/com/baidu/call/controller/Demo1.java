package com.baidu.call.controller;

/**
 * 使用递归求1+2+3+4+……+n的和
 */
public class Demo1 {
    public static void main(String[] args) {
        System.out.println(method(1));
    }

    public static int method(int n) {
        if (n > 0) {
            return n + method(n - 1);
        } else {
            return 0;
        }
    }
}
