/*
 * 描述：文件标签关联表
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.FileSizeUtil;

@Alias("TFileSign")
public class FileSign implements Serializable {
    /** 分享的文件ID **/
    private Integer           fileId;

    /** 标签ID **/
    private Integer           signId;

    /**员工id**/
    private Integer           employeeId;

    /**所属文件夹编码**/
    private String            folderCode;

    /**********************辅助字段***********************/

    /** 文件名称 **/
    private String            fileName;

    /** 文件类型 **/
    private Integer           fileType;

    /** 文件大小(B为单位) **/
    private Long              fileSize;

    /** 文件更新时间 **/
    private Date              updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getSignId() {
        return signId;
    }

    public void setSignId(Integer signId) {
        this.signId = signId;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getShowFileSize() {
        return FileSizeUtil.bytesToSize(fileSize);
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }
}
