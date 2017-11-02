package com.yunip.model.authority;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 员工角色与权限关联
 */
@Alias("TCpRolePermission")
public class CpRolePermission implements Serializable {
    /**角色id**/
    private Integer           roleId;

    /**权限id*/
    private Integer           permissionId;

    /***************辅助字段*****************/

    private List<Integer>     roleIds;

    private static final long serialVersionUID = 1L;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

}
