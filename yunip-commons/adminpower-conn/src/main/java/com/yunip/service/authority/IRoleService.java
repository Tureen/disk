package com.yunip.service.authority;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.authority.Role;
import com.yunip.model.authority.RolePermission;
import com.yunip.model.authority.query.RoleQuery;

/**
 * @author ming.zhu
 * 角色功能Service
 */
public interface IRoleService {
    /**
     * 保存角色信息（新增/修改）
     * @param role 角色信息
     * boolean
     */
    @Transactional
    boolean saveOrUpdate(Role role);

    /**
     * 根据角色ID查询角色信息
     * @param roleid 角色ID
     * @return 角色信息
     */
    Role getRole(Integer roleId);

    /**
     * 角色条件查询
     * @param query 查询条件
     * @return 满足条件的角色List
     */
    RoleQuery queryRoles(RoleQuery query);

    /**
     * 根据角色ID查询角色权限
     * @param roleid 角色ID
     * @return 
     * List<RoleFunc>
     */
    List<RolePermission> getRolePermission(Integer roleId);

    /**
     * 更改状态
     * @param roleid 
     * @param validStatus
     * void
     */
    @Transactional
    void updateValidStatus(Integer roleId,int validStatus,String systemCode);
    
    /**
     * 删除角色
     * @param roleId
     * @param systemCode  
     * void 
     * @exception
     */
    @Transactional
    int deleteRole(Integer roleId,String systemCode);

}
