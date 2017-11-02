package com.yunip.model.company;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/***
 * @author ming.zhu
 * 功能：工作组的简易输出对象
 */
@Alias("TSimpleWorkgroupData")
public class SimpleWorkgroupData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键**/
    private Integer           id;

    /**工作组名称**/
    private String            workgroupName;

    /**描述**/
    private String            remark;

    /**所属员工id**/
    private Integer           employeeId;

    /**所属员工名**/
    private String            createAdmin;

    /**加入状态**/
    private Integer           joinStatus;

    /**有效状态**/
    private Integer           validStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(Integer joinStatus) {
        this.joinStatus = joinStatus;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

}
