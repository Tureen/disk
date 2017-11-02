package com.yunip.model.company;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/***
 * @author ming.zhu
 * 功能：部门的简易输出对象
 */
@Alias("TSimpleEmpData")
public class SimpleEmpData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键**/
    private String            id;

    /**名称**/
    private String            employeeName;

    /**首字母**/
    private String            employeeChar;

    /**父部门ID**/
    private String            deptId;

    /**员工手机**/
    private String            mobile;

    /**头像路径**/
    private String            employeePortrait;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeChar() {
        return employeeChar;
    }

    public void setEmployeeChar(String employeeChar) {
        this.employeeChar = employeeChar;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmployeePortrait() {
        return employeePortrait;
    }

    public void setEmployeePortrait(String employeePortrait) {
        this.employeePortrait = employeePortrait;
    }
}
