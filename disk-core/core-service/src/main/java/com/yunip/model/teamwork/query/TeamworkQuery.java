/*
 * 描述：协作信息类
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.model.teamwork.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.enums.disk.ValidStatus;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.utils.page.PageQuery;

@Alias("TTeamworkQuery")
public class TeamworkQuery extends PageQuery<Teamwork> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -1486657216982882474L;

    /**协作名称**/
    private String            teamworkName;

    /**创建者id(管理员id)**/
    private Integer           employeeId;

    /**有效状态（启用:1,冻结:0）**/
    private Integer           validStatus      = ValidStatus.NOMAL.getStatus();

    /******************ADMIN*************************/
    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    /**关联协作员工id**/
    private Integer           teamworkEmployeeId;

    /**协作查询类型**/
    private Integer           teamworkSearchType;

    public String getTeamworkName() {
        return teamworkName;
    }

    public void setTeamworkName(String teamworkName) {
        this.teamworkName = teamworkName;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getTeamworkEmployeeId() {
        return teamworkEmployeeId;
    }

    public void setTeamworkEmployeeId(Integer teamworkEmployeeId) {
        this.teamworkEmployeeId = teamworkEmployeeId;
    }

    public Integer getTeamworkSearchType() {
        return teamworkSearchType;
    }

    public void setTeamworkSearchType(Integer teamworkSearchType) {
        this.teamworkSearchType = teamworkSearchType;
    }

}
