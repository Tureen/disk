package com.yunip.service;

import java.util.List;

import com.yunip.model.authority.CpPermission;

/**
 * @author ming.zhu
 * 员工权限业务层
 */
public interface ICpPermissionService {

    /**
     * 获取所有有效权限
     * @return  
     * List<EmployeePermission> 
     * @exception
     */
    List<CpPermission> getPermissions();
}
