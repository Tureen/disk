/*
 * 描述：文件伪删除信息表
 * 创建人：ming.zhu
 * 创建时间：2016-11-16
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.FileSizeUtil;

@Alias("TFileDelete")
public class FileDelete implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 旧文件id*/
    private Integer           oldId;

    /** 文件名称 **/
    private String            fileName;

    /** 文件后缀名 **/
    private String            fileSuffix;

    /** 文件类型(txt,jpeg,word,excel,pdf等) **/
    private Integer           fileType;

    /** 员工ID **/
    private Integer           employeeId;

    /** 文件大小(B为单位) **/
    private Long              fileSize;

    /** 文件夹ID **/
    private Integer           folderId;

    /** 所属文件夹编码 **/
    private String            folderCode;

    /** 文件存储地址 **/
    private String            filePath;

    /** 文件状态：0.伪删除 1.已删除 2.已还原 **/
    private Integer           status;

    /** 删除类型  0:直接删除 其他:对应folder_delete中old_id **/
    private Integer           deleteType;

    /** 删除此文件的员工id **/
    private Integer           actionEmployeeId;

    /** 删除员工ip地址 **/
    private String            actionEmployeeIp;

    /** 修改状态的管理员id **/
    private Integer           actionAdminId;

    /** 文件夹绝对路径，用“/”分割 **/
    private String            absolutePath;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;
    
    /** 标识文件加密状态 **/
    private Integer           encryptStatus;
    
    /** 加密密钥 **/
    private String           encryptKey;
    
    /** 所属文件服务器编号 **/
    private String           serverCode;

    /****************************************/

    /** 拥有者名称 **/
    private String            employeeName;

    /** 操作管理员 **/
    private String            adminName;

    private static final long serialVersionUID = 1L;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getShowFileSize() {
        return FileSizeUtil.bytesToSize(fileSize);
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

    public String getActionEmployeeIp() {
        return actionEmployeeIp;
    }

    public void setActionEmployeeIp(String actionEmployeeIp) {
        this.actionEmployeeIp = actionEmployeeIp;
    }

    public Integer getEncryptStatus() {
        return encryptStatus;
    }

    public void setEncryptStatus(Integer encryptStatus) {
        this.encryptStatus = encryptStatus;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }
}
