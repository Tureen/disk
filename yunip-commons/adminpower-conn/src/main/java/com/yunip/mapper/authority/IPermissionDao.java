package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.query.PermissionQuery;

/**
 * @author ming.zhu
 * 权限功能查询类
 */
@Repository("iPermissionDao")
public interface IPermissionDao extends IBaseDao<Permission>{
    /**
     * 
     * selectCountByQuery(条件查询，查询结果数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(PermissionQuery query);
    
    /**
     * 
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<Permission> 
     * @exception
     */
    List<Permission> selectByQuery(PermissionQuery query);
    
    /**
     * selectByAdminId(根据adminId获取所属权限对象集合) 
     * @param adminId
     * @return  
     * List<Permission> 
     * @exception
     */
    List<Permission> selectByAdminId(Integer adminId);
    
}
