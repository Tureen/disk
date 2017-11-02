/*
 * 描述：〈描述〉
 * 创建人：junbin.zhou
 * 创建时间：2012-8-24
 */
package com.yunip.service.authority.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.authority.IRoleDao;
import com.yunip.mapper.authority.IRolePermissionDao;
import com.yunip.mapper.user.IAdminRoleDao;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.Role;
import com.yunip.model.authority.RolePermission;
import com.yunip.model.authority.query.RoleQuery;
import com.yunip.model.user.AdminRole;
import com.yunip.service.authority.IRoleService;

/**
 * IRoleService 接口实现
 */
@Service("iRoleService")
public class RoleServiceImpl implements IRoleService {
    /** Dao **/
    @Resource(name = "iRoleDao")
    private IRoleDao           roleDao;

    @Resource(name = "iRolePermissionDao")
    private IRolePermissionDao rolePermissionDao;
    
    @Resource(name = "iAdminRoleDao")
    private IAdminRoleDao adminRoleDao;

    /**
     * 保存角色信息（新增/修改）
     * @param role 角色信息
     * boolean
     */
    public boolean saveOrUpdate(Role role) {
        int result = 0;
        if (role.getId() == null) {
            role.setValidStatus(1);
            result = roleDao.insert(role);
        } else {
            //相关权限全部删除再添加
            rolePermissionDao.delByRoleId(role.getId());
            result = roleDao.update(role);
        }
        if(role.getPermissions() != null && role.getPermissions().size()>0){
            for(Permission per : role.getPermissions()){
                if(per.getId()==null){
                    continue;
                }
                RolePermission rp = new RolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(per.getId());
                rolePermissionDao.insert(rp);
            }
        }
        return result > 0;
    }

    /**
     * 根据角色ID查询角色信息
     * @param roleid 角色ID
     * @return 角色信息
     */
    public Role getRole(Integer roleId) {
        return roleDao.selectById(roleId);
    }

    /**
     * 角色条件查询
     * @param query 查询条件
     * @return 满足条件的角色List
     */
    public RoleQuery queryRoles(RoleQuery query) {
        List<Role> list = roleDao.selectByQuery(query);
        int count = roleDao.selectCountByQuery(query);
        query.setRecordCount(count);
        query.setList(list);
        return query;
    }

    /**
     * 根据角色ID查询角色权限
     * @param roleid 角色ID
     * @return 
     * List<RoleFunc>
     */
    public List<RolePermission> getRolePermission(Integer roleId) {
        List<RolePermission> rolePermissions = rolePermissionDao.selectByRoleId(roleId);
        return rolePermissions;
    }

    /**
     * 更改状态
     * @param roleid 
     * @param validStatus
     * void
     */
    public void updateValidStatus(Integer roleId,int validStatus,String systemCode) {
        Role role = new Role();
        role.setId(roleId);
        role.setValidStatus(validStatus);
        role.setSystemCode(systemCode);
        roleDao.update(role);
    }

    /**
     * 删除角色
     */
    public int deleteRole(Integer roleId, String systemCode) {
        //判断是否有用户引用该角色
        List<AdminRole> adminRoles = adminRoleDao.selectByRoleId(roleId);
        if(adminRoles.size() > 0){
            return 1;
        }
        Role role = new Role();
        role.setId(roleId);
        role.setSystemCode(systemCode);
        roleDao.delById(role);
        rolePermissionDao.delByRoleId(roleId);
        return 0;
    }

}
