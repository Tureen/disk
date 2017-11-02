package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.RolePermission;

/**
 * @author ming.zhu
 * 角色与权限关系Dao
 */
@Repository("iRolePermissionDao")
public interface IRolePermissionDao extends IBaseDao<RolePermission> {
    
    /**
     * selectByRoleId(条件查询,根据roleId) 
     * @param roleId
     * @return  
     * List<RolePermission> 
     * @exception
     */
    List<RolePermission> selectByRoleId(Integer roleId);
    
    /**
     * delByRoleId(删除，通过rold) 
     * @param roleId
     * @return  
     * int 
     * @exception
     */
    int delByRoleId(Integer roleId);
}
