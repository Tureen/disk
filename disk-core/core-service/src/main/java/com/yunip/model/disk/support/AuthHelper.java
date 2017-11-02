/*
 * 描述：〈权限帮助类〉
 * 创建人：can.du
 * 创建时间：2016-5-23
 */
package com.yunip.model.disk.support;

import java.util.ArrayList;
import java.util.List;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;

/**
 * 权限帮助类
 */
public class AuthHelper {

    /**选中的部门ID**/
    private List<String>   deptIds      = new ArrayList<String>();

    /**员工ID**/
    private List<Employee> employees    = new ArrayList<Employee>();

    /**工作组id**/
    private List<Integer>  workgroupIds = new ArrayList<Integer>();

    /**是否选择全部**/
    private boolean        all          = false;

    /**管理权限类型**/
    private int            authType;

    /**文件夹或者文件ID**/
    private int            openId;

    /**操作类型（1.文件夹。2.文件夹ID）**/
    private int            openType;

    /**多文件夹或者文件夹保存的json数据**/
    private String         data;

    /**多文件或者文件夹保存的对象数据**/
    private Folder         folder;

    public List<String> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public int getOpenId() {
        return openId;
    }

    public void setOpenId(int openId) {
        this.openId = openId;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public List<Integer> getWorkgroupIds() {
        return workgroupIds;
    }

    public void setWorkgroupIds(List<Integer> workgroupIds) {
        this.workgroupIds = workgroupIds;
    }
}
