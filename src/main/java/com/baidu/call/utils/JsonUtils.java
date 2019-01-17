package com.baidu.call.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtils {
	private static final Logger logger = LogManager.getLogger(JsonUtils.class);
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @throws IOException
	 */
	public static void writeJson(Object object, HttpServletResponse httpServletResponse) {
		try {
			// SerializeConfig serializeConfig = new SerializeConfig();
			// serializeConfig.setAsmEnable(false);
			// String json = JSON.toJSONString(object);
			// String json = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
			//以前采用的方式
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			//可解决数据库中字段为null或者为空时转换的json字符串不包含该字段的问题
//			String json = JSON.toJSONString(object,SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
			// String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
			httpServletResponse.setContentType("application/json;charset=utf-8");
			httpServletResponse.getWriter().write(json);
			httpServletResponse.getWriter().flush();
		} catch (IOException e) {
			logger.debug(ExceptionUtil.getExceptionMessage(e));
		}
	}

	/**
	 * 将对象转换成序列化JSON字符串，并响应回前台
	 * @param object
	 * @throws IOException
	 */
	public static void writeJsonBySerializer(Object object, HttpServletResponse httpServletResponse) {
		try {
			// SerializeConfig serializeConfig = new SerializeConfig();
			// serializeConfig.setAsmEnable(false);
			// String json = JSON.toJSONString(object);
			// String json = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
			//以前采用的方式
//			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			//可解决数据库中字段为null或者为空时转换的json字符串不包含该字段的问题
			String json = JSON.toJSONString(object,SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
			// String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
			httpServletResponse.setContentType("application/json;charset=utf-8");
			httpServletResponse.getWriter().write(json);
			httpServletResponse.getWriter().flush();
		} catch (IOException e) {
			logger.debug(ExceptionUtil.getExceptionMessage(e));
		}
	}
}
