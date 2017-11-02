/*
 * 描述：〈管理员处理实现类〉
 * 创建人：can.du
 * 创建时间：2016-4-20
 */
package com.yunip.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.enums.AdminPermissionCode;
import com.yunip.mapper.authority.IPermissionDao;
import com.yunip.mapper.authority.IRolePermissionDao;
import com.yunip.mapper.user.IAdminDao;
import com.yunip.mapper.user.IAdminPermissionDao;
import com.yunip.mapper.user.IAdminRoleDao;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.Role;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminPermission;
import com.yunip.model.user.AdminRole;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.user.IAdminService;

/**
 * 管理员处理实现类
 */
@Service("iAdminService")
public class AdminServiceImpl implements IAdminService {

    @Resource(name = "iAdminDao")
    private IAdminDao           adminDao;

    @Resource(name = "iAdminRoleDao")
    private IAdminRoleDao       adminRoleDao;

    @Resource(name = "iAdminPermissionDao")
    private IAdminPermissionDao adminPermissionDao;

    @Resource(name = "iRolePermissionDao")
    private IRolePermissionDao  rolePermissionDao;

    @Resource(name = "iPermissionDao")
    private IPermissionDao      permissionDao;
    
    /**
     * 根据adminId获取权限集合
     */
    public List<Permission> getPermissions(Integer adminId) {
        return permissionDao.selectByAdminId(adminId);
    }

    /**
     * 登录用户信息获取
     */
    public Admin getLoginAdmin(AdminQuery query) {
        List<Admin> admins = adminDao.selectByQuery(query);
        if (admins != null && admins.size() > 0) {
            return admins.get(0);
        }
        return null;
    }

    /**
     * 条件查询列表
     */
    public AdminQuery queryAdmins(AdminQuery query) {
        List<Admin> list = adminDao.selectByQuery(query);
        int count = adminDao.selectCountByQuery(query);
        query.setRecordCount(count);
        query.setList(list);
        return query;
    }

    /**
     * 添加或编辑用户
     */
    public Admin saveOrUpdate(Admin admin) {
        if (admin.getId() == null) {
            admin.setValidStatus(1);
            adminDao.insert(admin);
        }
        else {
            adminDao.update(admin);
            //相关角色全部清空
            adminRoleDao.delByAdminId(admin.getId() == null ? 0 : admin.getId());
            //TODO 相关权限全部清空
            //adminPermissionDao.delByAdminId(admin.getId() == null ? 0 : admin.getId());
        }
        if (admin.getId() != null && admin.getId() > 0
                && admin.getRoles() != null) {
            //添加角色与用户关系
            for (Role role : admin.getRoles()) {
                if (role.getId() == null) {
                    continue;
                }
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(admin.getId());
                adminRole.setRoleId(role.getId());
                adminRoleDao.insert(adminRole);
            }
        }
        if (admin.getPermissions() != null && admin.getPermissions().size() > 0) {
            //添加用户与权限关系
            for (Permission per : admin.getPermissions()) {
                if (per.getId() == null) {
                    continue;
                }
                AdminPermission adminPermission = new AdminPermission();
                adminPermission.setAdminId(admin.getId());
                adminPermission.setPermissionId(per.getId());
                adminPermissionDao.insert(adminPermission);
            }
        }
        return admin;
    }

    /**
     * 根据adminId获取管理员与角色关系集合
     */
    public List<AdminRole> getAdminRole(Integer adminId) {
        return adminRoleDao.selectByAdminId(adminId);
    }

    /**
     * 修改管理员状态
     */
    public void updateValidStatus(Integer adminId, int validStatus) {
        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setValidStatus(validStatus);
        adminDao.update(admin);
    }

    /**
     * 删除管理员
     */
    public void deleteAdmin(Integer adminId) {
        adminDao.delById(adminId);
        //相关角色关系全部清空
        adminRoleDao.delByAdminId(adminId);
    }

    @Override
    public int updateAdmin(Admin admin) {
        return adminDao.update(admin);
    }

    /**
     * 根据用户ID查询用户权限
     * @param adminId 用户ID
     * @return 
     */
    public List<AdminPermission> getAdminPermission(Integer adminId) {
        List<AdminPermission> adminPermissions = adminPermissionDao.selectByAdminId(adminId);
        return adminPermissions;
    }

    @Override
    public void addAdminPermission(Integer adminId) {
        List<Integer> permissionIds = new ArrayList<Integer>();
        AdminPermissionCode[] adminPermissionCodes = AdminPermissionCode.values();
        for (AdminPermissionCode pcode : adminPermissionCodes) {
            permissionIds.add(pcode.getCode());
        }
        //添加用户与权限关系
        for (Integer id : permissionIds) {
            if (id == null) {
                continue;
            }
            AdminPermission adminPermission = new AdminPermission();
            adminPermission.setAdminId(adminId);
            adminPermission.setPermissionId(id);
            adminPermissionDao.insert(adminPermission);
        }
    }
}
