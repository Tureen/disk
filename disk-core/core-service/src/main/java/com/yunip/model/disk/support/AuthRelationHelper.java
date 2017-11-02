/*
 * 描述：〈权限关联帮助类〉
 * 创建人：ming.zhu
 * 创建时间：2017-1-5
 */
package com.yunip.model.disk.support;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限帮助类
 */
public class AuthRelationHelper {

    /**选中的部门名**/
    private List<String> deptNames      = new ArrayList<String>();

    /**员工名**/
    private List<String> employeeNames  = new ArrayList<String>();

    /**工作组名**/
    private List<String> workgroupNames = new ArrayList<String>();

    /**是否选择全部**/
    private boolean      all            = false;

    public List<String> getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(List<String> deptNames) {
        this.deptNames = deptNames;
    }

    public List<String> getEmployeeNames() {
        return employeeNames;
    }

    public void setEmployeeNames(List<String> employeeNames) {
        this.employeeNames = employeeNames;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public List<String> getWorkgroupNames() {
        return workgroupNames;
    }

    public void setWorkgroupNames(List<String> workgroupNames) {
        this.workgroupNames = workgroupNames;
    }

}
