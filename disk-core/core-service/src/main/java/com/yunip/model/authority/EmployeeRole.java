package com.yunip.model.authority;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/***
 * @author ming.zhu
 * 员工与角色信息
 */
@Alias("TEmployeeRole")
public class EmployeeRole implements Serializable {

    /** 员工id **/
    private Integer           employeeId;

    /** 角色id **/
    private Integer           roleId;

    private static final long serialVersionUID = 1L;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}
