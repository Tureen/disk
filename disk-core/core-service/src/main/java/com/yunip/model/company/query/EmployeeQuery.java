package com.yunip.model.company.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.company.Employee;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 前台账户查询对象
 */
@Alias("TEmployeeQuery")
public class EmployeeQuery extends PageQuery<Employee> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -4019445886799150458L;

    /** 员工ID **/
    private Integer           id;

    /** 部门ID */
    private String            deptId;

    /**手机号码*/
    private String            employeeMobile;

    /** 员工编号 **/
    private String            employeeCode;

    /** 真实名称 **/
    private String            employeeName;

    /** 登录表示 **/
    private String            token;

    /**************************辅助字段*****************************/

    /** 是否是管理员 0:管理员 1:员工 **/
    private Integer           isAdmin;

    /** 冻结状态 **/
    private Integer           adminValidStatus;

    /** 部门名称 **/
    private String            deptName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getEmployeeMobile() {
        return employeeMobile;
    }

    public void setEmployeeMobile(String employeeMobile) {
        this.employeeMobile = employeeMobile;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getAdminValidStatus() {
        return adminValidStatus;
    }

    public void setAdminValidStatus(Integer adminValidStatus) {
        this.adminValidStatus = adminValidStatus;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

}
