package com.yunip.service.authority;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.authority.Permission;
import com.yunip.model.authority.query.PermissionQuery;
import com.yunip.model.user.AdminAuthority;

/**
 * @author ming.zhu
 * 一句话功能描述
 */
public interface IPermissionService {
    /**
     * 保存or修改权限节点信息
     * @param Permission 
     * void
     */
    @Transactional
    int savePermission(Permission Permission);

    /**
     * 根据ID查询权限节点信息
     * @param Permissionid 权限节点ID
     * @return 
     * Permission
     */
    Permission getPermission(Integer Permissionid, String systemcode);
    
    /**
     * 更改状态
     * @param roleid 
     * @param validStatus
     * void
     */
    @Transactional
    void updateValidStatus(Integer roleId,int validStatus,String systemCode);

    /**
     * queryPermissions(条件查询，获取对象列表) 
     * @param query
     * @return  
     * PermissionQuery 
     * @exception
     */
    PermissionQuery queryPermissions(PermissionQuery query);

    /***
     * getParentPermissions(获取封装后的权限列表) 
     * @return  query
     * List<Permission> 
     * @exception
     */
    List<Permission> getParentPermissions(PermissionQuery query);
    
    
    /***
     * getadAdminAuthority(根据管理员ID获取管理员权限管理对象) 
     * @param adminId   管理员ID
     * @return  
     * AdminAuthority 
     * @exception
     */
    AdminAuthority getAdAdminAuthority(int adminId);
}
