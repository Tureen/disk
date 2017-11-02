/*
 * 描述：〈工作组的查询类〉
 * 创建人：ming.zhu
 * 创建时间：2017-1-18
 */
package com.yunip.model.disk.query;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.Workgroup;
import com.yunip.utils.page.PageQuery;

/**
 * 工作组的查询类
 */
@Alias("TWorkgroupQuery")
public class WorkgroupQuery extends PageQuery<Workgroup> {

    private static final long serialVersionUID = 1L;

    /**工作组名**/
    private String            workgroupName;

    /**所属员工id**/
    private Integer           employeeId;

    /**有效状态（启用:1,冻结:0）**/
    private Integer           validStatus;

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

}
