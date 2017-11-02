package com.yunip.model.authority;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 用户角色关联信息表
 */
@Alias("TRolePermission")
public class RolePermission implements Serializable {
    /**
     * 角色ID
     */
    private Integer           permissionId;

    /**
     * 权限ID
     */
    private Integer           roleId;

    private static final long serialVersionUID = 1L;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}
