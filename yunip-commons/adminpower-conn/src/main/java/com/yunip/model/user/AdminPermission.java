package com.yunip.model.user;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 员工权限关联信息表
 */
@Alias("TAdminPermission")
public class AdminPermission implements Serializable {
    /**
     * 员工ID
     */
    private Integer           adminId;

    /**
     * 权限ID
     */
    private Integer           permissionId;

    private static final long serialVersionUID = 1L;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}
