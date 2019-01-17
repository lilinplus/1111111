package com.baidu.call.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HisenUtils {
	/**
	 * 判断字符串是否为空
	 * 
	 * @param obj
	 * @return 非空时返回真
	 */
	public static boolean notEmpty(Object obj) {
		if (obj != null&&!obj.equals("null") && obj.toString().trim().length()>0)
			return true;
		return false;
	}

	/**
	 * 判断一个字符串能否被转为整数
	 * @return true 可以被转化
	 * @return false 不可用被转化
	 */
	public static boolean canParseInt(String str) {
		   try{
		       Integer.parseInt(str);
		     }catch(Exception e){
		       return false;
		    }
		  return true;
	}
	/**
	 * 将一个字符串数组转化为指定符号分隔的字符串
	 * @param strArray 待转化的数组
	 * @param separator 分隔符
	 * @return 转化后的字符串
	 */
	public static String stringArrayToStringWithSeparator( String[] strArray, String separator ) {
		if(strArray!=null){
			StringBuffer strbuf = new StringBuffer();
		    for( int i = 0; i < strArray.length; i++ ) {
		        strbuf.append( separator ).append( strArray[i] );
		    }
		       return strbuf.append( separator ).toString();
		    //  return strbuf.deleteCharAt( 0 ).toString();
		}else {
			return null;
		}
	}
	
	
	/**
	 * 使用反射机制
	 * 获取pojo中为null的字段数组 用于BeanUtils.copyporperties排除属性为null的字段
	 * 
	 * @param object 传入的bean
	 * @return
	 */
	public static String[] getNullAttrFromBean(Object object) {
		List<String> nullAttrList=new ArrayList<String>();
		if (object == null)
			return nullAttrList.toArray(new String[nullAttrList.size()]);
		Field[] fields = object.getClass().getDeclaredFields();
		String[] types1 = { "int", "java.lang.String", "boolean", "char", "float", "double", "long", "short", "byte" };
		String[] types2 = { "Integer", "java.lang.String", "java.lang.Boolean", "java.lang.Character", "java.lang.Float", "java.lang.Double", "java.lang.Long", "java.lang.Short", "java.lang.Byte" };
		for (int j = 0; j < fields.length; j++) {
			fields[j].setAccessible(true);
			// 字段名
		//	System.out.print(fields[j].getName() + ":");
			// 字段值
			for (int i = 0; i < types1.length; i++) {
				if (fields[j].getType().getName().equalsIgnoreCase(types1[i]) || fields[j].getType().getName().equalsIgnoreCase(types2[i])) {
					try {
				//		System.out.print(fields[j].get(object) + "     ");
						if(fields[j].get(object)==null){
							nullAttrList.add(fields[j].getName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return nullAttrList.toArray(new String[nullAttrList.size()]);
	}
	
	/**
	 * 
	 */
	public static String convertTimestampToYYMMDDHHSS(Timestamp timestamp){
	     //   Date date = new Date(timestamp.getTime());
		try {
			 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm");
			 return simpleDateFormat.format(timestamp);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static List<Integer> convertStrToIntArray(String str,String regex){
		List<Integer> intList=new ArrayList<Integer>();
		if(notEmpty(str)){
			String[] strArray=str.split(regex);
			for(String s:strArray){
				if(notEmpty(s)){
					try {
						Integer i=Integer.parseInt(s);
						intList.add(i);
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			return intList;
		}
		return intList;
	}

	/**
	 * 判断source数组中的值是否都包含于target数组中
	 * @param source
	 * @param target
     * @return true 都包含 false 不包含
     */
	public static boolean arrContinsArr(String[] source, String[] target) {
		for (String s : source) {
			if(Arrays.binarySearch(target, s)==-1){
				return false;
			}
		}
		return true;
	}
	
	/*
	public static void f1(int num) {
		if(num>3){
			f1(--num);
		}
		System.out.println(num);
	}
	
	public static void main(String[] args) {
		f1(5);
	}
	*/
}
