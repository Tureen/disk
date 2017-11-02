/*
 * 描述：文件删除索引类
 * 创建人：ming.zhu
 * 创建时间：2016-11-16
 */
package com.yunip.model.disk.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.FileDelete;
import com.yunip.utils.page.PageQuery;

@Alias("TFileDeleteQuery")
public class FileDeleteQuery extends PageQuery<FileDelete> implements
        Serializable {
    /**
     * 属性说明
     */
    private static final long serialVersionUID = 1000845702610993426L;

    /** 旧文件id*/
    private Integer           oldId;

    /** 文件名称 **/
    private String            fileName;

    /** 文件类型(txt,jpeg,word,excel,pdf等) **/
    private Integer           fileType;

    /** 员工ID **/
    private Integer           employeeId;

    /** 文件夹ID **/
    private Integer           folderId;

    /** 所属文件夹编码 **/
    private String            folderCode;

    /** 文件存储地址 **/
    private String            filePath;

    /** 文件状态：0.伪删除 1.已删除 2.已还原 **/
    private Integer           status;

    /** 删除类型  0:直接删除 1:通过上层文件夹删除 **/
    private Integer           deleteType;

    /** 文件夹绝对路径，用“/”分割 **/
    private String            absolutePath;

    /** 删除此文件的员工id **/
    private Integer           actionEmployeeId;

    /** 删除员工ip地址 **/
    private String            actionEmployeeIp;

    /** 修改状态的管理员id **/
    private Integer           actionAdminId;

    /******************************************/

    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    /**姓名**/
    private String            employeeName;

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getActionEmployeeIp() {
        return actionEmployeeIp;
    }

    public void setActionEmployeeIp(String actionEmployeeIp) {
        this.actionEmployeeIp = actionEmployeeIp;
    }

}
