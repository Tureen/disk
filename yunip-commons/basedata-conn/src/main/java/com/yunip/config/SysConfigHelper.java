/*
 * 描述：系统配置信息辅助类
 * 创建人：junbin.zhou
 * 创建时间：2012-9-10
 */
package com.yunip.config;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunip.mapper.config.ISysConfigDao;
import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;
import com.yunip.util.SpringContextUtil;

/**
 * 系统配置信息辅助类
 */
public final class SysConfigHelper {
    /** 日志组件 **/
    private static Logger log = Logger.getLogger(SysConfigHelper.class);
    
    /** 数据缓存是否加载完毕  **/
    private static boolean loaded = false;

    /** Map形式配置信息（包含 isvalid = false 的数据）**/
    private static HashMap<String, HashMap<String, String>> rootMap = new HashMap<String, HashMap<String, String>>();
    
    /**
     * 禁止实例化
     */
    private SysConfigHelper() {
    }
    
    /**
     * 
     * load(进行数据初始化) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param force 是否强制加载，用于重新初始化数据
     * void 
     * @exception
     */
    private static synchronized void load(boolean force) {
        //如果非强制初始化（重新初始化），并且已经初始化完毕，返回
        if (loaded && !force) {
            return;
        }
        
        log.info("========================== 加载系统静态配置信息到缓存开始 ==========================");
        loaded = false;
        rootMap.clear();
        
        //从数据库中获取数据
        ISysConfigDao mapper = SpringContextUtil.getBean("iSysConfigDao");
        SysConfigQuery query = new SysConfigQuery();
        query.setPageSize(Integer.MAX_VALUE);
        List<SysConfig> list = mapper.selectByQuery(query);
        
        //将数据加载到缓存
        loadSysConfig(list);
        
        loaded = true;
        log.info("========================== 加载系统静态配置信息到缓存结束 ==========================");
    }
    
    /**
     * 
     * reload(重新加载数据) 
     * (这里描述这个方法适用条件 – 可选)   
     * void 
     * @exception
     */
    public static void reload() {
        load(true);
    }
    
    /**
     * 
     * getValue(获取值) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param pcode 属性代码
     * @param pkey 属性key
     * @return  
     * String 
     * @exception
     */
    public static String getValue(String pcode, String pkey) {
        String pvalue = null;
        HashMap<String, String> keyMap = getMap(pcode);
        if (keyMap != null) {
            pvalue = keyMap.get(pkey);
        }
            
        if (pvalue == null) {
            pvalue = "";
        }
        return pvalue;
    }
    
    /**
     * 
     * getMap(获取某一类配置数据) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param pcode
     * @return  
     * HashMap<String,String> 
     * @exception
     */
    public static HashMap<String, String> getMap(String pcode) {
        if (!loaded) {
            load(false);
        }
        
        HashMap<String, String> keyMap = rootMap.get(pcode);
        if (keyMap == null) {
            return new HashMap<String, String>();
        }
        return keyMap;
    }
    
    /**
     * 
     * loadSysConfig(将数据加载到缓存中) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param list  
     * void 
     * @exception
     */
    private static void loadSysConfig(List<SysConfig> list) {
        for (SysConfig sysConfig : list) {
            HashMap<String, String> keyMap = rootMap.get(sysConfig.getConfigCode());
            if (keyMap == null) {
                keyMap = new HashMap<String, String>();
                rootMap.put(sysConfig.getConfigCode(), keyMap);
            }
            keyMap.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
        }
    }
    
}
