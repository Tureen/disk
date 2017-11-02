/*
 * 描述：文件索引操作信息表
 * 创建人：ming.zhu
 * 创建时间：2016-8-5
 */
package com.yunip.model.disk;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("TFileIndex")
public class FileIndex implements Serializable {
    /**主键**/
    private Integer           id;

    /**文件id**/
    private Integer           fileId;

    /**文件名称**/
    private String            fileName;

    /**文件类型(txt,jpeg,word,excel,pdf等)**/
    private Integer           fileType;

    /**员工ID**/
    private Integer           employeeId;

    /**文件存储地址**/
    private String            filePath;

    /**操作类型(1.新增.2.删除.3.更新)**/
    private Integer           operType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

}
