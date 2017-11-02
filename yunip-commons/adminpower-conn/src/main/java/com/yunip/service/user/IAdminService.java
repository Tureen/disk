/*
 * 描述：〈管理员接口类〉
 * 创建人：can.du
 * 创建时间：2016-4-20
 */
package com.yunip.service.user;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.authority.Permission;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminPermission;
import com.yunip.model.user.AdminRole;
import com.yunip.model.user.query.AdminQuery;

/**
 *  管理员接口类
 */
public interface IAdminService {
    
    /**
     * getPermissions(通过adminId获取所有所属权限对象集合) 
     * @param adminId
     * @return  
     * List<Permission> 
     * @exception
     */
    List<Permission> getPermissions(Integer adminId);

    /**
     * getLoginAdmin(登录查询管理员信息) 
     * @param query
     * @return  
     * Admin 
     * @exception
     */
    Admin getLoginAdmin(AdminQuery query);
    
    /**
     * queryAdmins(条件查询，获取对象列表) 
     * @param query
     * @return  
     * AdminQuery 
     * @exception
     */
    AdminQuery queryAdmins(AdminQuery query);
    
    /**
     * 修改用户
     * @param admin
     * @return  
     * int 
     * @exception
     */
    int updateAdmin(Admin admin);
    
    /**
     * 保存角色信息（新增/修改）
     * @param role 角色信息
     * boolean
     */
    @Transactional
    Admin saveOrUpdate(Admin admin);
    
    /**
     * 根据用户ID查询角色
     * @param adminId 用户ID
     * @return 
     * List<RoleFunc>
     */
    List<AdminRole> getAdminRole(Integer adminId);
    
    /**
     * 更改状态
     * @param adminId 
     * @param validStatus
     * void
     */
    @Transactional
    void updateValidStatus(Integer adminId,int validStatus);
    
    /**
     * 删除管理员
     * @param adminId
     * void 
     * @exception
     */
    @Transactional
    void deleteAdmin(Integer adminId);
    
    /**
     * 根据用户ID查询权限
     * @param adminId 用户ID
     * @return 
     */
    List<AdminPermission> getAdminPermission(Integer adminId);
    
    /**
     * 给予用户所有个人空间权限
     * addAdminPermission(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param adminId  
     * void 
     * @exception
     */
    void addAdminPermission(Integer adminId);
}
