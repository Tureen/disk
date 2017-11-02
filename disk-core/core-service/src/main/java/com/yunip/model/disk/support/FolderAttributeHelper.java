/*
 * 描述：〈文件夹属性〉
 * 创建人：ming.zhu
 * 创建时间：2017-1-5
 */
package com.yunip.model.disk.support;

import java.util.Date;
import java.util.List;

public class FolderAttributeHelper {
    
    private Integer folderId;

    private Date createTime;
    
    private Date updateTime;
    
    private String fileName;
    
    private String fileVersion;
    
    private List<String> signNames;
    
    private List<String> managerEmployees;
    
    private List<String> seeEmployees;
    
    private List<String> readEmployees;

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public List<String> getSignNames() {
        return signNames;
    }

    public void setSignNames(List<String> signNames) {
        this.signNames = signNames;
    }

    public List<String> getManagerEmployees() {
        return managerEmployees;
    }

    public void setManagerEmployees(List<String> managerEmployees) {
        this.managerEmployees = managerEmployees;
    }

    public List<String> getSeeEmployees() {
        return seeEmployees;
    }

    public void setSeeEmployees(List<String> seeEmployees) {
        this.seeEmployees = seeEmployees;
    }

    public List<String> getReadEmployees() {
        return readEmployees;
    }

    public void setReadEmployees(List<String> readEmployees) {
        this.readEmployees = readEmployees;
    }

    
}
