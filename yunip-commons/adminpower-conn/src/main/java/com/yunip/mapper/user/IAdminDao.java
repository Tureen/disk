package com.yunip.mapper.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.user.Admin;
import com.yunip.model.user.query.AdminQuery;

/***
 * 管理数据DAO
 */
@Repository("iAdminDao")
public interface IAdminDao extends IBaseDao<Admin>{
    
    /***
     * selectByQuery(根据条件查询对象) 
     * @param query 查询条件
     * @return  
     * Admin 
     * @exception
     */
    List<Admin> selectByQuery(AdminQuery query);
    
    /**
     * 
     * selectCountByQuery(根据条件查询对象数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(AdminQuery query);
    
}