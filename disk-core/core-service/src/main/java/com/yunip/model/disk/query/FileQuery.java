/*
 * 描述：文件索引类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.enums.disk.ValidStatus;
import com.yunip.model.disk.File;
import com.yunip.utils.page.PageQuery;

@Alias("TFileQuery")
public class FileQuery extends PageQuery<File> implements Serializable {
    /**
     * 属性说明
     */
    private static final long serialVersionUID = 3418478667768134291L;

    /** 文件名称 **/
    private String            fileName;

    /** 员工ID **/
    private Integer           employeeId;

    /** 文件夹ID **/
    private Integer           folderId;

    /** 文件版本 **/
    private Integer           fileVersion;

    /** 有效状态（预留字段） **/
    private Integer           validStatus      = ValidStatus.NOMAL.getStatus(); ;

    /** 分享状态 **/
    private Integer           shareStatus;

    /**文件名称模糊匹配**/
    private String            queryName;

    /**文件夹编码**/
    private String            folderCode;

    /**文件路径**/
    private String            filePath;

    /**排除不查询的文件ID**/
    private Integer           pFileId;
    
    /** 标识文件加密状态 **/
    private Integer           encryptStatus;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public Integer getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(Integer fileVersion) {
        this.fileVersion = fileVersion;
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

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public Integer getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(Integer shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public Integer getpFileId() {
        return pFileId;
    }

    public void setpFileId(Integer pFileId) {
        this.pFileId = pFileId;
    }

    public Integer getEncryptStatus() {
        return encryptStatus;
    }

    public void setEncryptStatus(Integer encryptStatus) {
        this.encryptStatus = encryptStatus;
    }
}
