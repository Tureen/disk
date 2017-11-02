/*
 * 描述：集群服务器配置信息辅助类
 * 创建人：jian.xiong
 * 创建时间：2016-12-21
 */
package com.yunip.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.yunip.mapper.config.IClusterConfigDao;
import com.yunip.model.config.ClusterConfig;
import com.yunip.model.config.query.ClusterConfigQuery;
import com.yunip.util.SpringContextUtil;

/**
 * 集群服务器配置信息辅助类
 */
public final class ClusterConfigHelper {
    /** 日志组件 **/
    private static Logger log = Logger.getLogger(ClusterConfigHelper.class);
    
    /** 数据缓存是否加载完毕  **/
    private static boolean loaded = false;

    /** Map形式配置信息（包含 isvalid = false 的数据）**/
    private static HashMap<String, ClusterConfig> rootMap = new HashMap<String, ClusterConfig>();
    
    /**
     * 禁止实例化
     */
    private ClusterConfigHelper() {
        
    }
    
    /**
     * load(进行数据初始化) 
     * @param force 是否强制加载，用于重新初始化数据
     */
    private static synchronized void load(boolean force) {
        //如果非强制初始化（重新初始化），并且已经初始化完毕，返回
        if (loaded && !force) {
            return;
        }
        
        log.info("========================== 加载集群服务器配置信息到缓存开始 ==========================");
        loaded = false;
        rootMap.clear();
        
        //从数据库中获取数据
        IClusterConfigDao mapper = SpringContextUtil.getBean("iClusterConfigDao");
        ClusterConfigQuery query = new ClusterConfigQuery();
        query.setPageSize(Integer.MAX_VALUE);
        List<ClusterConfig> list = mapper.selectByQuery(query);
        //将数据加载到缓存
        loadSysConfig(list);
        
        loaded = true;
        log.info("========================== 加载集群服务器配置信息到缓存结束 ==========================");
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
     * getValue(获取值) 
     * @param pcode 属性代码
     * @param pkey 属性key
     * @return  
     */
    public static ClusterConfig getValue(String pcode) {
        if (!loaded) {
            load(false);
        }
        return rootMap.get(pcode);
    }
    
    /**
     * getAllList(获取所有信息) 
     */
    public static List<ClusterConfig> getAllList() {
        if (!loaded) {
            load(false);
        }
        List<ClusterConfig> list = new ArrayList<ClusterConfig>();
        for(Entry<String, ClusterConfig> entry : rootMap.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }
    
    /**
     * 
     * loadSysConfig(将数据加载到缓存中) 
     * @param list  
     */
    private static void loadSysConfig(List<ClusterConfig> list) {
        for (ClusterConfig clusterConfig : list) {
            rootMap.put(clusterConfig.getClusterCode(), clusterConfig);
        }
    }
}