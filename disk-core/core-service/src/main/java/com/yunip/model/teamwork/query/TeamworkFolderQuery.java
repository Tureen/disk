/*
 * 描述：协作文件基本信息类
 * 创建人：ming.zhu
 * 创建时间：2017-02-27
 */
package com.yunip.model.teamwork.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.enums.disk.ValidStatus;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.utils.page.PageQuery;

@Alias("TTeamworkFolderQuery")
public class TeamworkFolderQuery extends PageQuery<TeamworkFolder> implements
        Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -1486657216982882474L;

    /** 主键 **/
    private Integer           folderId;

    /** 文件夹编码 **/
    private String            folderCode;

    /** 文件夹名称 **/
    private String            folderName;

    /** 员工ID **/
    private Integer           employeeId;

    /**协作ID**/
    private Integer           teamworkId;

    /**父文件夹Id**/
    private Integer           parentId;

    /**有效状态（启用:1,冻结:0）**/
    private Integer           validStatus      = ValidStatus.NOMAL.getStatus();

    /**查询名称**/
    private String            queryName;

    /***排除本身的ID***/
    private Integer           pFolderId;

    /******************ADMIN*************************/
    /***员工名称***/
    private String            employeeName;

    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    /**部门Id**/
    private String            deptId;

    /**部门名称**/
    private String            deptName;

    /**排序类型**/
    private String            orderIndex;

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTeamworkId() {
        return teamworkId;
    }

    public void setTeamworkId(Integer teamworkId) {
        this.teamworkId = teamworkId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Integer getpFolderId() {
        return pFolderId;
    }

    public void setpFolderId(Integer pFolderId) {
        this.pFolderId = pFolderId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

}
