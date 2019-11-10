package com.baidu.call.config;

import org.jasypt.util.text.BasicTextEncryptor;

public class Encryptor {

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("EbfYkitulv73I2p0mXI50JMXoaxZTKJ0");
        String url = textEncryptor.encrypt("jdbc:mysql://localhost:3306/zycoo_coovox");
        String name = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("123456");
//解密内容
        String url1 = textEncryptor.decrypt("");
        String name1 = textEncryptor.decrypt("");
        String password1 = textEncryptor.decrypt("4EyN0xDLbnP2lsaayjl8fbIctj5bVIdD");


        System.out.println(url);
        System.out.println(name);
        System.out.println(password);
    }
}
