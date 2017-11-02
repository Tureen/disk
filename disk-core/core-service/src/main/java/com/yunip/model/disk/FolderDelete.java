package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("TFolderDelete")
public class FolderDelete implements Serializable {

    /** 主键 **/
    private Integer            id;

    /** 旧id **/
    private Integer            oldId;

    /** 文件夹编码 **/
    private String             folderCode;

    /** 员工ID **/
    private Integer            employeeId;

    /** 文件夹名称 **/
    private String             folderName;

    /** 文件类型(1.私人,2.公共) **/
    private Integer            folderType;

    /** 父文件ID **/
    private Integer            parentId;

    /** 文件夹状态 0.伪删除 1.已删除 2.已还原 **/
    private Integer            status;

    /** 删除类型  0:直接删除  其他:对应old_id **/
    private Integer            deleteType;

    /** 删除此文件的员工id **/
    private Integer            actionEmployeeId;

    /** 删除员工ip地址 **/
    private String             actionEmployeeIp;

    /** 修改状态的管理员id **/
    private Integer            actionAdminId;

    /** 文件夹绝对路径，用“/”分割 **/
    private String             absolutePath;

    /** 创建人 **/
    private String             createAdmin;

    /** 创建时间 **/
    private Date               createTime;

    /** 更新人 **/
    private String             updateAdmin;

    /** 更新时间 **/
    private Date               updateTime;

    /*******************************************/

    /** 删除此文件的员工姓名 **/
    private String             employeeName;

    /** 对回收站文件or文件夹操作的管理员 **/
    private String             adminName;

    /** 文件夹集合（存在于父文件夹） **/
    private List<FolderDelete> folderDeletes;

    /** 文件集合（存在于父文件夹） **/
    private List<FileDelete>   fileDeletes;

    private static final long  serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOldId() {
        return oldId;
    }

    public void setOldId(Integer oldId) {
        this.oldId = oldId;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getActionEmployeeId() {
        return actionEmployeeId;
    }

    public void setActionEmployeeId(Integer actionEmployeeId) {
        this.actionEmployeeId = actionEmployeeId;
    }

    public Integer getActionAdminId() {
        return actionAdminId;
    }

    public void setActionAdminId(Integer actionAdminId) {
        this.actionAdminId = actionAdminId;
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

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Integer getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(Integer deleteType) {
        this.deleteType = deleteType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<FolderDelete> getFolderDeletes() {
        return folderDeletes;
    }

    public void setFolderDeletes(List<FolderDelete> folderDeletes) {
        this.folderDeletes = folderDeletes;
    }

    public List<FileDelete> getFileDeletes() {
        return fileDeletes;
    }

    public void setFileDeletes(List<FileDelete> fileDeletes) {
        this.fileDeletes = fileDeletes;
    }

    public String getActionEmployeeIp() {
        return actionEmployeeIp;
    }

    public void setActionEmployeeIp(String actionEmployeeIp) {
        this.actionEmployeeIp = actionEmployeeIp;
    }

}
