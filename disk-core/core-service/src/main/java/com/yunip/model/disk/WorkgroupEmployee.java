/*
 * 描述：工作组员工关联表
 * 创建人：ming.zhu
 * 创建时间：2017-01-18
 */
package com.yunip.model.disk;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("TWorkgroupEmployee")
public class WorkgroupEmployee implements Serializable {
    /**工作组id**/
    private Integer           workgroupId;

    /**关联员工id**/
    private Integer           employeeId;

    /**************************************/

    private String            employeeName;

    private String            deptName;

    private String            employeeEmail;

    private static final long serialVersionUID = 1L;

    public Integer getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Integer workgroupId) {
        this.workgroupId = workgroupId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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
