/*
 * 描述：协作信息表
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.model.teamwork;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.FileSizeUtil;
import com.yunip.utils.util.IconUtil;

@Alias("TTeamwork")
public class Teamwork implements Serializable {
    /**主键**/
    private Integer           id;

    /**协作名称**/
    private String            teamworkName;

    /**协作图标**/
    private Integer           icon;

    /**有效状态（启用:1,冻结:0）**/
    private Integer           validStatus;

    /**创建者id(管理员id)**/
    private Integer           employeeId;

    /**备注**/
    private String            remark;

    /**创建人**/
    private String            createAdmin;

    /**创建时间**/
    private Date              createTime;

    /**更新人**/
    private String            updateAdmin;

    /**更新时间**/
    private Date              updateTime;

    /**************************************************************/
    /**已用空间**/
    private Long              useSpaceSize;
    
    /**拥有者名称**/
    private String            employeeName;

    /**成员数量**/
    private Integer           employeeNumber;

    /**id集合**/
    private List<Integer>     ids;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamworkName() {
        return teamworkName;
    }

    public void setTeamworkName(String teamworkName) {
        this.teamworkName = teamworkName;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getUseSpaceSize() {
        return useSpaceSize;
    }

    public void setUseSpaceSize(Long useSpaceSize) {
        this.useSpaceSize = useSpaceSize;
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getShowFileSize() {
        return FileSizeUtil.bytesToSize(useSpaceSize);
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
    
    public String getIconStr(){
        return IconUtil.teamworkIcon(icon);
    }
}
