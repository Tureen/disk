package com.yunip.model.disk.support;

import java.util.List;

import com.yunip.model.disk.Sign;

public class SignReq {

    /** 文件id **/
    private Integer    fileId;

    /** 所属员工id **/
    private Integer    employeeId;

    /** 创建人 **/
    private String     createAdmin;

    /** 更新人 **/
    private String     updateAdmin;

    /** 标签集合 **/
    private List<Sign> signs;

    public List<Sign> getSigns() {
        return signs;
    }

    public void setSigns(List<Sign> signs) {
        this.signs = signs;
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

    public String getCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(String createAdmin) {
        this.createAdmin = createAdmin;
    }

    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

}
