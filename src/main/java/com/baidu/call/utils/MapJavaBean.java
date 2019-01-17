package com.baidu.call.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyafei01_sh on 2018/5/3.
 */
public class MapJavaBean {
    /**
     * map转Javabean
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * javabean转map
     *
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    /**
     * List深copy(集合中含有不同的对象可copy)
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List copyList(List<T> list, Object obj) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JSON.parseArray(JSON.toJSONString(list), obj.getClass());
    }

}
