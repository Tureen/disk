/*
 * 描述：协作信息员工关联表
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.model.teamwork;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("TTeamworkEmployee")
public class TeamworkEmployee implements Serializable {
    /**员工id**/
    private Integer           employeeId;

    /**协作id**/
    private Integer           teamworkId;

    /**权限类型 1:最高权限**/
    private Integer           authorityType;

    /*********************************************************/

    /**员工名称**/
    private String            employeeName;

    /**部门名称**/
    private String            deptName;

    /**员工邮箱**/
    private String            employeeEmail;

    private static final long serialVersionUID = 1L;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTeamworkId() {
        return teamworkId;
    }

    public void setTeamworkId(Integer teamworkId) {
        this.teamworkId = teamworkId;
    }

    public Integer getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(Integer authorityType) {
        this.authorityType = authorityType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

}
