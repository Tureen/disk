package com.yunip.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.SerialCode;
import com.yunip.model.query.SerialCodeQuery;

/**
 * @author ming.zhu
 * 序列号DAO
 */
@Repository("iSerialCodeDao")
public interface ISerialCodeDao extends IBaseDao<SerialCode>{
    /**
     * 
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<Permission> 
     * @exception
     */
    List<SerialCode> selectByQuery(SerialCodeQuery query);
}
