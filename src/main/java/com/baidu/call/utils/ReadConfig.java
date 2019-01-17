package com.baidu.call.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {

    private static Logger logger = LogManager.getLogger(ReadConfig.class);

    /*
     * read properties 文件
     */
    public static String readConfig(String fileName, String key) {
        Properties pps = new Properties();
        try {
            InputStream in = ReadConfig.class.getResource("/" + fileName + ".properties").openStream();
            pps.load(in);
            String value = pps.getProperty(key);
            in.close();
            return value;
        } catch (IOException e) {
            logger.error(e);
        }
        return null;

    }

    /*
 * read properties 文件
 */
    public static String readConfigByPath(String path, String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new FileInputStream(path);
//            InputStream in = ReadConfig.class.getResource("/" + fileName + ".properties").openStream();
            pps.load(in);
            String value = pps.getProperty(key);
            in.close();
            return value;
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

//    public static void main(String[] args) {
//        System.out.println(readConfigByPath("D:\\dist\\email.properties", "username"));
//    }


}
