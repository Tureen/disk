package com.yunip.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/***
 * @author can.du
 */
public final class SpringContextUtil implements ApplicationContextAware {
	
	public static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		SpringContextUtil.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) ctx.getBean(name);
	}
}
