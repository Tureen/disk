package com.yunip.model.authority.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.authority.CpRole;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 角色管理查询条件类
 */
@Alias("TCpRoleQuery")
public class CpRoleQuery extends PageQuery<CpRole> implements
        Serializable {

    private static final long serialVersionUID = -4033363800957907915L;

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

    /**角色类型: 1:默认角色包 2:个人专属角色**/
    private Integer           roleType;

    /**员工id:角色类型为2时，需有值**/
    private Integer           employeeId;

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

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

}
