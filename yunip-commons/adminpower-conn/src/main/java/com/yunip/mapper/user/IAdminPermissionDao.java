package com.yunip.mapper.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.user.AdminPermission;

/**
 * @author ming.zhu
 * 用户与权限关系
 */
@Repository("iAdminPermissionDao")
public interface IAdminPermissionDao extends IBaseDao<AdminPermission>{
    /**
     * selectByAdminId(条件查询,根据adminId) 
     * @param adminId
     * @return  
     * List<AdminPermission> 
     * @exception
     */
    List<AdminPermission> selectByAdminId(Integer adminId);
    
    /**
     * delByAdminId(删除，通过adminId) 
     * @param adminId
     * @return  
     * int 
     * @exception
     */
    int delByAdminId(Integer adminId);
}
