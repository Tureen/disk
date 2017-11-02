package com.yunip.model.user;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/***
 * @author ming.zhu
 * 后台管理员与角色信息
 */
@Alias("TAdminRole")
public class AdminRole implements Serializable {
    
    /** 管理员id **/
    private Integer           adminId;

    /** 角色id **/
    private Integer           roleId;

    private static final long serialVersionUID = 1L;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}
