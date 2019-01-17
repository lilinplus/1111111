package com.baidu.call.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by v_chenyafei on 2017/3/10.
 */
public class SetterByReflect {
    /**
     * 动态注入jpa查询条件和参数，字符串类型为like查询
     *
     * @param params
     * @param objCla
     * @param root
     * @param cb
     * @return
     */
    public static List<Predicate> setterAttribute(Map<String, Object> params, Class objCla, Root<?> root, CriteriaBuilder cb) {
        List<Predicate> lstPredicates = new ArrayList<Predicate>();
        /**
         * 得到类中的所有属性集合
         */
        Field[] fs = objCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); //设置些属性是可以访问的
            String type = f.getType().toString();
            if (type.endsWith("String")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.like(root.get(f.getName()).as(String.class), "%" + params.get(f.getName()) + "%"));
                    }
                }
            } else if (type.endsWith("long")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(Long.class), Long.parseLong(params.get(f.getName()).toString())));
                    }
                }
            } else if (type.endsWith("int") || type.endsWith("Integer")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(Integer.class), Integer.parseInt(params.get(f.getName()).toString())));
                    }
                }
            } else if (type.endsWith("boolean")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(boolean.class), Boolean.parseBoolean(params.get(f.getName()).toString())));
                    }
                }
            }
        }
        return lstPredicates;
    }


    /**
     * 动态注入jpa查询条件和参数，字符串类型为精确查询查询
     *
     * @param params
     * @param objCla
     * @param root
     * @param cb
     * @return
     */
    public static List<Predicate> setterAttributeEqual(Map<String, Object> params, Class objCla, Root<?> root, CriteriaBuilder cb) {
        List<Predicate> lstPredicates = new ArrayList<Predicate>();
        /**
         * 得到类中的所有属性集合
         */
        Field[] fs = objCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); //设置些属性是可以访问的
            String type = f.getType().toString();
            if (type.endsWith("String")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(String.class), params.get(f.getName())));
                    }
                }
            } else if (type.endsWith("long")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(Long.class), Long.parseLong(params.get(f.getName()).toString())));
                    }
                }
            } else if (type.endsWith("int") || type.endsWith("Integer")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(Integer.class), Integer.parseInt(params.get(f.getName()).toString())));
                    }
                }
            } else if (type.endsWith("boolean")) {
                if (params != null && params.containsKey(f.getName())) {
                    if (TextUtils.isNotBlank(params.get(f.getName()).toString())) {
                        lstPredicates.add(cb.equal(root.get(f.getName()).as(boolean.class), Boolean.parseBoolean(params.get(f.getName()).toString())));
                    }
                }
            }
        }
        return lstPredicates;
    }
}
