package com.yunip.memcached;

import java.util.Date;

public class MemCachedClient {
	
	/**
	 * 说明； 将对象放入到 内存中,代码暂未实现
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期时间
	 * @return ture 成功 false 失败
	 */
	public  boolean set(String key, Object value, Date expiry) {
		return MemCachedClientFactory.getMemcachedClient().set(key, value, expiry);
	}
	
	/**
	 * 说明； 将对象放入到 内存中
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期的秒数
	 * @return ture 成功 false 失败
	 */
	public  boolean set(String key, Object value, int expiry) {
		return set(key, value, getPreTime(expiry));
	}
	/**
	 * 说明； 将对象放入到 内存中,代码暂未实现,默认缓存10分钟
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return ture 成功 false 失败
	 */
	public  boolean set(String key, Object value) {
		return MemCachedClientFactory.getMemcachedClient().set(key, value);
	}

	/**
	 * 说明：将内存中的对象进行替换
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期时间
	 * @return ture 成功 false 失败
	 */
	public  boolean replace(String key, Object value) {
		return false;
	}

	/**
	 * 说明：将内存中的对象进行替换
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期的秒数
	 * @return ture 成功 false 失败
	 */
	public  boolean replace(String key, Object value, int expiry) {
		return false;
	}
	/**
	 * 说明：将内存中的对象进行替换,代码暂未实现
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期时间
	 * @return ture 成功 false 失败
	 */
	public  boolean replace(String key, Object value, Date expiry) {
		return false;
	}
	/**
	 * 说明：通过key获取对象
	 * 
	 * @param key
	 *            key
	 * @return
	 */
	public  Object get(String key) {
		return MemCachedClientFactory.getMemcachedClient().get(key);
	}

	/**
	 * 说明； 将对象添加到 内存中,暂时未实现
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期时间
	 * @return ture 成功 false 失败
	 */
	public  boolean add(String key, Object value, Date expiry) {
		return false;
	}
	/**
	 * 说明； 将对象添加到 内存中
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expiry
	 *            过期秒数
	 * @return ture 成功 false 失败
	 */
	public  boolean add(String key, Object value, int expiry) {
		return false;
	}
	/**
	 * 说明； 将对象添加到 内存中.默认10分钟
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return ture 成功 false 失败
	 */
	public  boolean add(String key, Object value) {
		return false;
	}
	/**
	 * 说明； 通过key将对象删除
	 * 
	 * @param key
	 *            键
	 * @return ture 成功 false 失败
	 */
	public  boolean delete(String key) {
		return MemCachedClientFactory.getMemcachedClient().delete(key);
	}
	
	/**
	 * 说明：将当前时间前移的秒数
	 * @param expiry 秒数
	 * @return
	 */
	private Date getPreTime(int expiry) {
		Date newDate = new Date();
		Long time = newDate.getTime() + expiry * 1000;
		newDate.setTime(time);
		return newDate;
	}

}
