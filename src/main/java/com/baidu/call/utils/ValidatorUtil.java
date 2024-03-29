package com.baidu.call.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.*;

/**
 * Created by v_chenyafei on 2017/12/7.
 * model实体的验证
 */
public class ValidatorUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    /**
     * 返回验证信息为map格式
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> Map<String,StringBuffer> validate(T obj){
        Map<String,StringBuffer> errorMap = null;
        Set<ConstraintViolation<T>> set = validator.validate(obj,Default.class);
        if(set != null && set.size() >0 ){
            errorMap = new HashMap<String,StringBuffer>();
            String property = null;
            for(ConstraintViolation<T> cv : set){
                //这里循环获取错误信息，可以自定义格式
                property = cv.getPropertyPath().toString();
                if(errorMap.get(property) != null){
                    errorMap.get(property).append("," + cv.getMessage());
                }else{
                    StringBuffer sb = new StringBuffer();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap;
    }


    /**
     * 返回验证信息为list格式
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> List validateList(T obj){
        List errorList = null;
        Set<ConstraintViolation<T>> set = validator.validate(obj,Default.class);
        if(set != null && set.size() >0 ){
            errorList = new ArrayList();
            String property = null;
            for(ConstraintViolation<T> cv : set){
                //这里循环获取错误信息，可以自定义格式
                StringBuffer sb = new StringBuffer();
                sb.append(cv.getMessage());
                errorList.add(sb);
            }
        }
        return errorList;
    }
}
