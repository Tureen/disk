/*
 * 描述：〈权限帮助类〉
 * 创建人：can.du
 * 创建时间：2016-5-23
 */
package com.yunip.model.disk.support;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 权限帮助类
 */
public class AuthHelperApi {

    /**选中的部门ID**/
    private List<String>        deptIds      = new ArrayList<String>();

    /**员工ID**/
    private List<Integer>       employeeIds  = new ArrayList<Integer>();

    /**工作组id**/
    private List<Integer>       workgroupIds = new ArrayList<Integer>();

    /**是否选择全部**/
    private boolean             all          = false;

    /**目标文件夹ID**/
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer             id;

    /**操作的文件夹列表其中ReqSameName中的ID为文件夹ID**/
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<AuthHelperApi> folders      = new ArrayList<AuthHelperApi>();

    /**操作的文件夹列表其中ReqSameName中的ID为文件ID**/
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<AuthHelperApi> files        = new ArrayList<AuthHelperApi>();

    public AuthHelperApi() {
        super();
    }

    public AuthHelperApi(List<String> deptIds, List<Integer> employeeIds,
            List<Integer> workgroupIds, boolean all, Integer id,
            List<AuthHelperApi> folders, List<AuthHelperApi> files) {
        super();
        this.deptIds = deptIds;
        this.employeeIds = employeeIds;
        this.workgroupIds = workgroupIds;
        this.all = all;
        this.id = id;
        this.folders = folders;
        this.files = files;
    }

    public List<String> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<AuthHelperApi> getFolders() {
        return folders;
    }

    public void setFolders(List<AuthHelperApi> folders) {
        this.folders = folders;
    }

    public List<AuthHelperApi> getFiles() {
        return files;
    }

    public void setFiles(List<AuthHelperApi> files) {
        this.files = files;
    }

    public List<Integer> getWorkgroupIds() {
        return workgroupIds;
    }

    public void setWorkgroupIds(List<Integer> workgroupIds) {
        this.workgroupIds = workgroupIds;
    }

}
