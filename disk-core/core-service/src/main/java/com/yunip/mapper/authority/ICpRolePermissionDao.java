package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.CpRolePermission;

/**
 * @author ming.zhu
 * 角色与权限关系Dao
 */
@Repository("iCpRolePermissionDao")
public interface ICpRolePermissionDao extends IBaseDao<CpRolePermission> {
    
    /**
     * selectByRoleId(条件查询,根据roleId) 
     * @param roleId
     * @return  
     * List<EmployeeRolePermission> 
     * @exception
     */
    List<CpRolePermission> selectByRoleId(Integer roleId);
    
    /**
     * delByRoleId(删除，通过rold) 
     * @param roleId
     * @return  
     * int 
     * @exception
     */
    int delByRoleId(Integer roleId);
    
    /**
     * 查询角色集合对应的不重复权限
     * @param cpRolePermission
     * @return  
     * List<CpRolePermission> 
     * @exception
     */
    List<CpRolePermission> selectByRoleIds(CpRolePermission cpRolePermission);
}
