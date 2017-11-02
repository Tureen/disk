/*
 * 描述：角色管理查询条件类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-23
 */
package com.yunip.model.authority.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.authority.Role;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 角色管理查询条件类
 */
@Alias("TRoleQuery")
public class RoleQuery extends PageQuery<Role> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 7553037971790798951L;
    
    /** 角色id **/
    private Integer           id;

    /** 角色名称，用于模糊查询 **/
    private String            roleName;

    /** 系统代码 **/
    private String            systemCode;

    /** 有效状态(1.冻结，0.正常) **/
    private Integer           validStatus;

    /** 角色描述 **/
    private String            roleDesc;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
