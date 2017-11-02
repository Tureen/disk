/*
 * 描述：工作组申请表
 * 创建人：ming.zhu
 * 创建时间：2017-02-04
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("TWorkgroupApply")
public class WorkgroupApply implements Serializable {
    /**主键**/
    private Integer           id;

    /**申请的工作组id**/
    private Integer           workgroupId;

    /**申请者员工id**/
    private Integer           applyEmployeeId;

    /**申请状态 0:待审核 1:审核通过 2:审核拒绝**/
    private Integer           applyStatus;

    /**创建人（申请人）**/
    private String            createAdmin;

    /**创建时间（申请时间）**/
    private Date              createTime;

    /**修改人（审批人）**/
    private String            updateAdmin;

    /**修改时间（审批时间）**/
    private Date              updateTime;

    /********************************************/

    /**申请组名**/
    private String            workgroupName;

    /**申请员工姓名**/
    private String            applyEmployeeName;

    /**工作组所属人姓名**/
    private String            createEmployeeName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Integer workgroupId) {
        this.workgroupId = workgroupId;
    }

    public Integer getApplyEmployeeId() {
        return applyEmployeeId;
    }

    public void setApplyEmployeeId(Integer applyEmployeeId) {
        this.applyEmployeeId = applyEmployeeId;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
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

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public String getApplyEmployeeName() {
        return applyEmployeeName;
    }

    public void setApplyEmployeeName(String applyEmployeeName) {
        this.applyEmployeeName = applyEmployeeName;
    }

    public String getCreateEmployeeName() {
        return createEmployeeName;
    }

    public void setCreateEmployeeName(String createEmployeeName) {
        this.createEmployeeName = createEmployeeName;
    }

}
