/*
 * 描述：〈文件版本的查询类〉
 * 创建人：can.du
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.Contact;
import com.yunip.utils.page.PageQuery;

/**
 * 文件版本的查询类
 */
@Alias("TContactQuery")
public class ContactQuery extends PageQuery<Contact> {

    private static final long serialVersionUID = 1L;

    /**员工id**/
    private Integer           employeeId;

    /**联系人id**/
    private Integer           contactId;

    /*************************************************/

    /**员工姓名**/
    private String            employeeName;

    /**部门名称**/
    private String            deptName;

    /**部门id**/
    private Integer           deptId;
    
    /** 管理员状态 **/
    private Integer           adminValidStatus;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getAdminValidStatus() {
        return adminValidStatus;
    }

    public void setAdminValidStatus(Integer adminValidStatus) {
        this.adminValidStatus = adminValidStatus;
    }

}
