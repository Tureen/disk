package com.yunip.mapper.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.user.AdminRole;

/**
 * @author ming.zhu
 * 用户与角色关系
 */
@Repository("iAdminRoleDao")
public interface IAdminRoleDao extends IBaseDao<AdminRole>{
    /**
     * selectByRoleId(条件查询,根据roleId) 
     * @param roleId
     * @return  
     * List<AdminRole> 
     * @exception
     */
    List<AdminRole> selectByRoleId(Integer roleId);
    
    /**
     * selectByAdminId(条件查询,根据adminId) 
     * @param adminId
     * @return  
     * List<AdminRole> 
     * @exception
     */
    List<AdminRole> selectByAdminId(Integer adminId);
    
    /**
     * delByAdminId(删除，通过adminId) 
     * @param adminId
     * @return  
     * int 
     * @exception
     */
    int delByAdminId(Integer adminId);
}
