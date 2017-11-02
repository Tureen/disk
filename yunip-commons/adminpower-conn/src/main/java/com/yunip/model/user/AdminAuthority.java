/*
 * 描述：〈管理员权限使用对象〉
 * 创建人：can.du
 * 创建时间：2016-4-25
 */
package com.yunip.model.user;

import java.io.Serializable;
import java.util.List;

import com.yunip.model.authority.Permission;

/**
 * 管理员权限使用对象
 */
public class AdminAuthority implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**权限对象列表(首页导航级权限)**/
    List<Permission> permissions;
    
    /**所有子集权限**/
    List<String> codes;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
