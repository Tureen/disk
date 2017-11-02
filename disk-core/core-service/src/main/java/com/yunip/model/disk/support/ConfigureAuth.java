/*
 * 描述：〈配置权限的辅助类〉
 * 创建人：can.du
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.support;

import java.io.Serializable;
import java.util.List;

/**
 * 配置权限的辅助类
 */
public class ConfigureAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**分享的对象ID（文件夹ID或者文件ID）**/
    private int               openId;

    /**分享的类型(文件夹或者文件)**/
    private int               type;

    private int               auth;

    private int               shareType;

    private List<Integer>     employeeId;

    private List<String>      deptId;

    public int getOpenId() {
        return openId;
    }

    public void setOpenId(int openId) {
        this.openId = openId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public List<Integer> getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(List<Integer> employeeId) {
        this.employeeId = employeeId;
    }

    public List<String> getDeptId() {
        return deptId;
    }

    public void setDeptId(List<String> deptId) {
        this.deptId = deptId;
    }
}
