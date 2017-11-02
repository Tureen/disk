/*
 * 描述：协作记录信息表
 * 创建人：ming.zhu
 * 创建时间：2017-03-16
 */
package com.yunip.model.teamwork;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.enums.disk.FileType;

@Alias("TTeamworkMessage")
public class TeamworkMessage implements Serializable {
    /**主键**/
    private Integer           id;

    /**组id**/
    private Integer           zid;

    /**消息类型 1:上传  2:删除  3:下载  4:新建文件夹  5:导出  6:留言 7:重命名**/
    private Integer           msgType;

    /**标题**/
    private String            title;

    /**内容**/
    private String            content;

    /**员工id**/
    private Integer           employeeId;

    /**协作id**/
    private Integer           teamworkId;

    /**文件id**/
    private Integer           fileId;

    /**文件夹id**/
    private Integer           folderId;

    /**创建人**/
    private String            createAdmin;

    /**创建时间**/
    private Date              createTime;

    /**更新人**/
    private String            updateAdmin;

    /**更新时间&*/
    private Date              updateTime;

    /********************************************/

    /**员工姓名**/
    private String            employeeName;

    /**员工头像**/
    private String            employeePortrait;

    /**部门名称**/
    private String            deptName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePortrait() {
        return employeePortrait;
    }

    public void setEmployeePortrait(String employeePortrait) {
        this.employeePortrait = employeePortrait;
    }

    public Integer getFileType() {
        FileType fileType = FileType.getFileType(content);
        return fileType.getType();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

}
