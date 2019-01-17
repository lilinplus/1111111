package com.baidu.call.utils;

/**
 * Created by v_chenyafei on 2017/1/13.
 */
public class TextUtils {

    public static boolean isNotBlank(String param){
        if(param.equals("") && param.length()==0){
            return false;
        }else{
            return true;
        }
    }
}
