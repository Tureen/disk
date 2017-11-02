/*
 * 描述：qly_bd_sysconfig 表查询类
 * 创建人：junbin.zhou
 * 创建时间：2012-9-10
 */
package com.yunip.mapper.config;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;

/**
 * 配置信息查询类
 */
@Repository("iSysConfigDao")
public interface ISysConfigDao extends IBaseDao<SysConfig>{
    /**
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<SysConfig> 
     * @exception
     */
    List<SysConfig> selectByQuery(SysConfigQuery query);
    
    /**
     * selectCountByQuery(条件查询，查询结果数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(SysConfigQuery query);
}
