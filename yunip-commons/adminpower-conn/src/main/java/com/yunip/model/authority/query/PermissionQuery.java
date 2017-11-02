/*
 * 描述：权限管理查询条件类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-23
 */
package com.yunip.model.authority.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.authority.Permission;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 权限管理查询条件类
 */
@Alias("TPermissionQuery")
public class PermissionQuery extends PageQuery<Permission> implements
        Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8469177772635381643L;

    /** 权限id **/
    private Integer           id;

    /** 权限名称 **/
    private String            permissionName;

    /** 父节点 ID **/
    private Integer           permissionFid;

    /** 导航URL(导航级权限具备) **/
    private String            permissionUrl;

    /** 权限类型 **/
    private Integer           permissionType;

    /** 系统代码 **/
    private String            systemCode;

    /** 有效状态（启用:1,冻结:0） **/
    private Integer           validStatus;

    /** 排序编号 **/
    private Integer           sortNo;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Integer getPermissionFid() {
        return permissionFid;
    }

    public void setPermissionFid(Integer permissionFid) {
        this.permissionFid = permissionFid;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
