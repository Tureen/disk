/*
 * 描述：qly_bd_reference 访问类
 * 创建人：junbin.zhou
 * 创建时间：2012-9-10
 */
package com.yunip.mapper.config;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.config.Reference;
import com.yunip.model.config.query.ReferenceQuery;

/**
 * qly_bd_reference 访问类
 */
@Repository("iReferenceDao")
public interface IReferenceDao extends IBaseDao<Reference>{
    /**
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<SysConfig> 
     * @exception
     */
    List<Reference> selectByQuery(ReferenceQuery query);
    
    /**
     * selectCountByQuery(条件查询，查询结果数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(ReferenceQuery query);
}
