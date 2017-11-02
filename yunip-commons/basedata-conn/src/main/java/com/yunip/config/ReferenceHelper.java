/*
 * 描述：系统参照信息辅助类
 * 创建人：junbin.zhou
 * 创建时间：2012-9-10
 */
package com.yunip.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunip.mapper.config.IReferenceDao;
import com.yunip.model.config.Reference;
import com.yunip.model.config.query.ReferenceQuery;
import com.yunip.util.SpringContextUtil;

/**
 * 系统参照信息辅助类
 * 参照信息：
 *        1. 系统自定义的各类状态、分类信息
 *        2. 主要属性为 key 和 value
 */
public final class ReferenceHelper {
    /** 日志组件 **/
    private static Logger log = Logger.getLogger(ReferenceHelper.class);
    
    /** 数据缓存是否加载完毕  **/
    private static boolean loaded = false;
    
    /** 列表形式配置信息（不包含 isvalid = false 的数据）**/
    private static HashMap<String, List<Reference>> listMap = new HashMap<String, List<Reference>>();
    /** 列表形式配置信息（包含 isvalid = false 的数据）**/
    private static HashMap<String, List<Reference>> allListMap = new HashMap<String, List<Reference>>();
    
    /** Map形式配置信息（包含 isvalid = false 的数据）**/
    private static HashMap<String, HashMap<String, String>> rootMap = new HashMap<String, HashMap<String, String>>();
   
    /**
     * 禁止实例化
     */
    private ReferenceHelper() {
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
        
        log.info("========================== 加载系统参照信息到缓存开始 ==========================");
        loaded = false;
        listMap.clear();
        rootMap.clear();
        
        //从数据库中获取数据
        IReferenceDao mapper = SpringContextUtil.getBean("iReferenceDao");
        ReferenceQuery query = new ReferenceQuery();
        query.setPageIndex(Integer.MAX_VALUE);
        List<Reference> list = mapper.selectByQuery(query);
        
        //将数据加载到缓存
        loadReferences(list);
        
        loaded = true;
        log.info("========================== 加载系统参照信息到缓存结束 ==========================");
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
     * getList(获取列表数据) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param pcode
     * @return  
     * List<Properties> 
     * @exception
     */
    public static List<Reference> getList(String pcode){
        if(!loaded){
            load(false);
        }
        List<Reference> list = listMap.get(pcode);
        if(list == null) {
            list = new ArrayList<Reference>();
        }
        return list;
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
        if (!loaded) {
            load(false);
        }
        String pvalue = null;
        HashMap<String, String> keyMap = rootMap.get(pcode);
        if (keyMap != null) {
            pvalue = keyMap.get(pkey);
        }
        if (pvalue == null) {
            pvalue = pkey;
        }
        return pvalue;
    }
    
    /**
     * 
     * loadProperties(将数据加载到缓存中) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param list  
     * void 
     * @exception
     */
    private static void loadReferences(List<Reference> list) {
        for (Reference properties : list) {
            //如果 ValidStatus = 1(启用) ，添加到列表
            if (properties.getValidStatus() == 1) {
                List<Reference> keyList = listMap.get(properties.getRefCode());
                if (keyList == null) {
                    keyList = new ArrayList<Reference>();
                    listMap.put(properties.getRefCode(), keyList);
                }
                keyList.add(properties);
            }
            //不管 ValidStatus 为何值 ，都添加到列表
            List<Reference> keyList = allListMap.get(properties.getRefCode());
            if (keyList == null) {
                keyList = new ArrayList<Reference>();
                allListMap.put(properties.getRefCode(), keyList);
            }
            keyList.add(properties);
            //无论 ValidStatus 为何值 ，都添加到map
            HashMap<String, String> keyMap = rootMap.get(properties.getRefCode());
            if (keyMap == null) {
                keyMap = new HashMap<String, String>();
                rootMap.put(properties.getRefCode(), keyMap);
            }
            keyMap.put(properties.getRefKey(), properties.getRefValue());
        }
    }
    /**
     * 
     * getList(获取列表数据,不管是否有效) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param pcode
     * @return  
     * List<Properties> 
     * @exception
     */
    public static List<Reference> getAllList(String pcode){
        if(!loaded){
            load(false);
        }
        List<Reference> list = allListMap.get(pcode);
        if(list == null) {
            list = new ArrayList<Reference>();
        }
        return list;
    }
}
