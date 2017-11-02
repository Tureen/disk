/*
 * 描述：文件提取码信息表
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.FileSizeUtil;

@Alias("TTakeCode")
public class TakeCode implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 提取码 **/
    private String            takeCode;

    /** 文件id **/
    private Integer           fileId;

    /** 所属文件夹编码 **/
    private String            folderCode;

    /** 员工ID **/
    private Integer           employeeId;

    /** 失效时间 **/
    private Date              effectiveTime;

    /** 有效天数 **/
    private Integer           effectiveDate;

    /** 剩余下载数量 **/
    private Integer           remainDownloadNum;

    /** 备注 **/
    private String            remark;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    /******************************************************/

    /** 文件名称 **/
    private String            fileName;

    /** 文件大小 **/
    private Long              fileSize;

    /** 员工姓名 **/
    private String            employeeName;

    /** 文件类型 **/
    private Integer           fileType;

    /** 文件更新时间 **/
    private Date              fUpdateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Integer getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Integer effectiveDate) {
        this.effectiveDate = effectiveDate;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Date getfUpdateTime() {
        return fUpdateTime;
    }

    public void setfUpdateTime(Date fUpdateTime) {
        this.fUpdateTime = fUpdateTime;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public String getShowFileSize() {
        return FileSizeUtil.bytesToSize(fileSize);
    }

    public Integer getRemainDownloadNum() {
        return remainDownloadNum;
    }

    public void setRemainDownloadNum(Integer remainDownloadNum) {
        this.remainDownloadNum = remainDownloadNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
