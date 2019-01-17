package com.baidu.call.utils;

/**
 * Created by chenyafei01_sh on 2018/8/31.
 */
public class StringToLongArray {

    /**
     * string 字符串转long数组
     *
     * @param str
     * @return
     */
    public static Long[] stringToLongArray(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        String strs[] = str.split(",");
        Long[] num = new Long[strs.length];
        for (int i = 0; i < strs.length; i++) {
            num[i] = Long.parseLong(strs[i]);
        }
        return num;
    }
}
