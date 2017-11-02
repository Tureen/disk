package com.yunip.model.authority;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 员工权限
 */
@Alias("TCpPermission")
public class CpPermission implements Serializable {
    /**主键**/
    private Integer           id;

    /**权限名称**/
    private String            permissionName;

    /**权限样式**/
    private String            permissionClass;

    /**权限描述**/
    private String            permissionDesc;

    /**权限code**/
    private Integer           permissionCode;

    /**有效状态**/
    private Integer           validStatus;

    /**系统代码**/
    private String            systemCode;

    /**创建人**/
    private String            createAdmin;

    /**创建时间**/
    private Date              createTime;

    /**更新人*/
    private String            updateAdmin;

    /**更新时间*/
    private Date              updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionClass() {
        return permissionClass;
    }

    public void setPermissionClass(String permissionClass) {
        this.permissionClass = permissionClass;
    }

    public String getPermissionDesc() {
        return permissionDesc;
    }

    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
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

    public String getCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(String createAdmin) {
        this.createAdmin = createAdmin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(Integer permissionCode) {
        this.permissionCode = permissionCode;
    }

}
