/*
 * 描述：〈工作组申请表的查询类〉
 * 创建人：ming.zhu
 * 创建时间：2017-02-04
 */
package com.yunip.model.disk.query;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.WorkgroupApply;
import com.yunip.utils.page.PageQuery;

/**
 * 工作组申请表的查询类
 */
@Alias("TWorkgroupApplyQuery")
public class WorkgroupApplyQuery extends PageQuery<WorkgroupApply> {

    private static final long serialVersionUID = 1L;

    /**申请的工作组id**/
    private Integer           workgroupId;

    /**申请者员工id**/
    private Integer           applyEmployeeId;

    /**申请状态**/
    private Integer           applyStatus;

    /*******************************************/

    /**工作组所属员工id**/
    private Integer           workgroupEmployeeId;

    /**工作组名**/
    private String            workgroupName;

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

    public Integer getWorkgroupEmployeeId() {
        return workgroupEmployeeId;
    }

    public void setWorkgroupEmployeeId(Integer workgroupEmployeeId) {
        this.workgroupEmployeeId = workgroupEmployeeId;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

}
