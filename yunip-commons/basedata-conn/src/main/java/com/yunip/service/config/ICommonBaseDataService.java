package com.yunip.service.config;

import java.util.HashMap;

import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;

/**
 * reference表，sysconfig表，region表的 维护功能Service接口。
 * 
 * @author Administrator
 * 
 */
public interface ICommonBaseDataService {


	/**
	 * 获得多条去旅游全局静态配置信息
	 * 
	 * @param sysConfigQuery
	 * @return
	 */
	SysConfigQuery listSysConfig(SysConfigQuery query);
	
	/**
	 * 获取map集合，根据code
	 * @param code
	 * @return  
	 * HashMap<String,String> 
	 * @exception
	 */
	HashMap<String, String> getSysConfigByCode(String code);

	/**
	 * 保存去旅游全局静态配置信息
	 * 
	 * @param sysConfig
	 * @return
	 */
	int saveSysConfig(SysConfig sysConfig);

	/**
	 * 获得单条去旅游全局静态配置信息
	 * 
	 * @param configcode
	 * @param configkey
	 * @return
	 */
	SysConfig getSysConfig(String configcode, String configkey);

	/**
	 * 修改去旅游全局静态配置信息
	 * 
	 * @param sysConfigQuery
	 * @return
	 */
	int editSysConfig(SysConfig sysConfig);

}
