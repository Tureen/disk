package com.yunip.model.authority;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.model.company.Employee;

/**
 * @author ming.zhu
 * 员工角色
 */
@Alias("TCpRole")
public class CpRole implements Serializable {
    /**主键**/
    private Integer            id;

    /**角色名称**/
    private String             roleName;

    /**角色描述*/
    private String             roleDesc;

    /**有效状态**/
    private Integer            validStatus;

    /**系统代码*/
    private String             systemCode;

    /**角色类型  1:默认角色包 2:个人专属角色*/
    private Integer            roleType;

    /**员工id 角色类型为2时，需有值**/
    private Integer            employeeId;

    /**创建人*/
    private String             createAdmin;

    /**创建时间**/
    private Date               createTime;

    /**更新人**/
    private String             updateAdmin;

    /**更新时间**/
    private Date               updateTime;

    /**************************辅助字段**************************/

    /**权限集合**/
    private List<CpPermission> cpPermissions;

    /**选中员工集合**/
    private List<Employee>     employees;

    /**全部员工集合**/
    private List<Employee>     allEmployees;

    private static final long  serialVersionUID = 1L;

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

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
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

    public List<CpPermission> getCpPermissions() {
        return cpPermissions;
    }

    public void setCpPermissions(List<CpPermission> cpPermissions) {
        this.cpPermissions = cpPermissions;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

}
