package com.yunip.model.teamwork;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.date.DateUtil;

@Alias("TSimpleTeamworkData")
public class SimpleTeamworkData implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 5270276316701660161L;

    /**主键**/
    private Integer           id;

    /**协作名称**/
    private String            teamworkName;

    /**协作图标**/
    private Integer           icon;

    /**备注**/
    private String            remark;

    /**创建者id(管理员id)**/
    private Integer           employeeId;

    /**创建时间**/
    private Date              createTime;

    /**已用空间**/
    private Long              useSpaceSize;

    /**拥有者名称**/
    private String            employeeName;

    /**成员数量**/
    private Integer           employeeNumber;

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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUseSpaceSize() {
        return useSpaceSize;
    }

    public void setUseSpaceSize(Long useSpaceSize) {
        this.useSpaceSize = useSpaceSize;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getCreateTimeFormat() {
        return DateUtil.getDateFormat(createTime, DateUtil.YMDSTRING_DATA);
    }

}
