/*
 * 描述：工作组表
 * 创建人：ming.zhu
 * 创建时间：2017-01-18
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("TWorkgroup")
public class Workgroup implements Serializable {
    /**主键**/
    private Integer           id;

    /**工作组名**/
    private String            workgroupName;

    /**所属员工id**/
    private Integer           employeeId;

    /**有效状态（启用:1,冻结:0）**/
    private Integer           validStatus;

    /**描述**/
    private String            remark;

    /**创建员工**/
    private String            createAdmin;

    /**创建时间**/
    private Date              createTime;

    /**修改员工**/
    private String            updateAdmin;

    /**修改时间**/
    private Date              updateTime;

    /***************************************************/

    /**id集合**/
    private List<Integer>     ids;

    private static final long serialVersionUID = 1L;

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

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

}
