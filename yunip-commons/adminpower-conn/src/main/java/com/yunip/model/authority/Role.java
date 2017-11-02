package com.yunip.model.authority;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.model.base.BaseModel;

/**
 * @author ming.zhu
 * 系统角色信息表
 */
@Alias("TRole")
public class Role extends BaseModel implements Serializable {
    /**
     * 主键
     */
    private Integer           id;

    /**
     * 角色名称
     */
    private String            roleName;

    /**
     * 角色描述
     */
    private String            roleDesc;

    /**
     * 状态(1.冻结，0.正常)
     */
    private Integer           validStatus;

    /**
     * 系统代码
     */
    private String            systemCode;

    /*******************************辅助字段**************************************/
    /***权限对象列表***/
    private List<Permission> permissions;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
