package com.baidu.call.controller;

/**
 * 一列数的规则如下: 1、1、2、3、5、8、13、21、34 ，求第30位数是多少？使用递归实现
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println(method(7));
    }

    public static int method(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return method(n - 1) + method(n - 2);
        }
    }
}
