/*
 * 描述：系统日志工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.util;

import org.apache.log4j.Logger;

/**
 * 系统日志工具类，采用log4j实现
 * 从  classpath 下读取 log4j.properties 配置
 */
public class Log {
	//禁止实例化
	private Log(){
		//org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
	}
	
	/**
	 * 输出 info 级别日志
	 * @param object 调用日志的对象，用于log4j判断日志级别
	 * @param message 日志内容
	 */
	public static void info(final Object object,final String message){
		Logger logger = Logger.getLogger(object.getClass());		
		logger.info(message);
	}
	
	/**
	 * 输出 info 级别日志
	 * @param object 调用日志的对象，用于log4j判断日志级别
	 * @param message 日志内容
	 */
	public static void debug(final Object object,final String message){
		Logger logger = Logger.getLogger(object.getClass());		
		logger.debug(message);
	}
	
	/**
	 * 输出 info 级别日志
	 * @param object 调用日志的对象，用于log4j判断日志级别
	 * @param message 日志内容
	 */
	public static void error(final Object object,final String message){
		Logger logger = Logger.getLogger(object.getClass());		
		logger.error(message);
	}
	
	/**
	 * 输出 info 级别日志
	 * @param object 调用日志的对象，用于log4j判断日志级别
	 * @param message 日志内容
	 */
	public static void warn(final Object object,final String message){
		Logger logger = Logger.getLogger(object.getClass());		
		logger.warn(message);
	}
}
