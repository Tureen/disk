package com.yunip.service.authority.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.enums.PermissionType;
import com.yunip.mapper.authority.IPermissionDao;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.query.PermissionQuery;
import com.yunip.model.user.AdminAuthority;
import com.yunip.service.authority.IPermissionService;

/**
 * @author ming.zhu
 * IPermissionService 接口实现
 */
@Service("iPermissionService")
public class PermissionServiceImpl implements IPermissionService {
    /** PermissionServiceBl **/
    @Resource(name = "iPermissionDao")
    private IPermissionDao permissionDao;

    /**
     * 保存or修改权限节点信息
     * @param Permission 
     * void
     */
    public int savePermission(Permission permission) {
        int sub = 0;
        if (permission.getId() == null) {
            sub = permissionDao.insert(permission);
        }
        else {
            sub = permissionDao.update(permission);
        }
        return sub;
    }

    /**
     * 根据ID查询权限节点信息
     * @param Permissionid 权限节点ID
     * @return 
     * Permission
     */
    public Permission getPermission(Integer id, String systemCode) {
        PermissionQuery query = new PermissionQuery();
        query.setId(id);
        query.setSystemCode(systemCode);
        Permission permission = permissionDao.selectByQuery(query).get(0);
        return permission;
    }

    /**
     * 更改状态
     */
    public void updateValidStatus(Integer permissionId,int validStatus,String systemCode){
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setSystemCode(systemCode);
        permission.setValidStatus(validStatus);
        permissionDao.update(permission);
    }

    /**
     * query条件查询
     */
    public PermissionQuery queryPermissions(PermissionQuery query) {
        List<Permission> list = permissionDao.selectByQuery(query);
        int count = permissionDao.selectCountByQuery(query);
        query.setRecordCount(count);
        query.setList(list);
        return query;
    }

    @Override
    public List<Permission> getParentPermissions(PermissionQuery query) {
        query.setPageSize(Integer.MAX_VALUE);
        List<Permission> permissions = permissionDao.selectByQuery(query);
        List<Permission> navList = new ArrayList<Permission>();
        Map<Integer, List<Permission>> sonMap = new HashMap<Integer, List<Permission>>();
        List<Permission> sonList = new ArrayList<Permission>();
        if(permissions != null && permissions.size() > 0){
            //循环
            for(Permission permission : permissions){
                if(permission.getPermissionType() == PermissionType.NAVIGATION.getType()){
                    //导航
                    navList.add(permission);
                } else {
                    if(sonMap.get(permission.getPermissionFid()) != null){
                        sonList = sonMap.get(permission.getPermissionFid());
                    } else {
                        sonList = new ArrayList<Permission>();
                    }
                    sonList.add(permission);
                    sonMap.put(permission.getPermissionFid(), sonList);
                }
            }
        }
        if(navList != null && navList.size() > 0){
            //循环
            for(Permission permission : navList){
                List<Permission> list = sonMap.get(permission.getId());
                if(list != null && list.size() > 0){
                    for(Permission perm : list){
                        List<Permission> sList = sonMap.get(perm.getId());
                        perm.setPermissions(sList);
                    }
                }
                permission.setPermissions(list);
            }
        }
        return navList;
    }

    @Override
    public AdminAuthority getAdAdminAuthority(int adminId) {
        List<Permission> permissions = permissionDao.selectByAdminId(adminId);
        AdminAuthority adminAuthority = new AdminAuthority();
        List<String> codes = new ArrayList<String>();
        List<Permission> navList = new ArrayList<Permission>();
        Map<Integer, List<Permission>> sonMap = new HashMap<Integer, List<Permission>>();
        List<Permission> sonList = new ArrayList<Permission>();
        if(permissions != null && permissions.size() > 0){
            //循环
            for(Permission permission : permissions){
                if(permission.getPermissionType() == PermissionType.NAVIGATION.getType()){
                    //导航
                    navList.add(permission);
                } else if(permission.getPermissionType() == PermissionType.PAGE.getType()){
                    if(sonMap.get(permission.getPermissionFid()) != null){
                        sonList = sonMap.get(permission.getPermissionFid());
                    } else {
                        sonList = new ArrayList<Permission>();
                    }
                    sonList.add(permission);
                    sonMap.put(permission.getPermissionFid(), sonList);
                }
                codes.add(permission.getPermissionCode());
            }
            //再次循环
            if(navList != null && navList.size() > 0){
                //循环
                for(Permission permission : navList){
                    //只需要做到页面级别
                    List<Permission> list = sonMap.get(permission.getId());
                    permission.setPermissions(list);
                }
            }
            adminAuthority.setCodes(codes);
            adminAuthority.setPermissions(navList);
        }
        return adminAuthority;
    }

}
