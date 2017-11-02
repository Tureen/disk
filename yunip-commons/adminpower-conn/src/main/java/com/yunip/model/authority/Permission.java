package com.yunip.model.authority;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.model.base.BaseModel;

/**
 * @author ming.zhu
 * 权限信息表
 */
@Alias("TPermission")
public class Permission extends BaseModel implements Serializable {
    /**
     * 主键
     */
    private Integer           id;

    /**
     * 权限名称
     */
    private String            permissionName;

    /**
     * 权限编码
     */
    private String            permissionCode;

    /**
     * 权限类型(1.导航级权限.1.页面级权限)
     */
    private Integer           permissionType;

    /**
     * 权限描述
     */
    private String            permissionDesc;

    /**
     * 权限父节点ID
     */
    private Integer           permissionFid;

    /**
     * 权限图标名称
     */
    private String            permissionIcon;

    /**
     * 导航URL(导航级权限具备)
     */
    private String            permissionUrl;

    /**
     * 排序编号
     */
    private Integer           sortNo;

    /**
     * 有效状态（启用:1,冻结:0）
     */
    private Integer           validStatus;

    /**
     * 系统代码
     */
    private String            systemCode;
    
    /*************************辅助字段*********************************/
    /***子权限列表**/
    private List<Permission> permissions;

    /***父权限名称***/
    private String            permissionFname;

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
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode == null ? null : permissionCode.trim();
    }

    public String getPermissionDesc() {
        return permissionDesc;
    }

    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc == null ? null : permissionDesc.trim();
    }

    public Integer getPermissionFid() {
        return permissionFid;
    }

    public void setPermissionFid(Integer permissionFid) {
        this.permissionFid = permissionFid;
    }

    public String getPermissionIcon() {
        return permissionIcon;
    }

    public void setPermissionIcon(String permissionIcon) {
        this.permissionIcon = permissionIcon == null ? null : permissionIcon.trim();
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl == null ? null : permissionUrl.trim();
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
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

    public String getPermissionFname() {
        return permissionFname;
    }

    public void setPermissionFname(String permissionFname) {
        this.permissionFname = permissionFname;
    }
}
