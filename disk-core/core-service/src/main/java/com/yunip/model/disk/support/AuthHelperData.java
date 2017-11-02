/*
 * 描述：〈权限帮助类〉
 * 创建人：can.du
 * 创建时间：2016-5-23
 */
package com.yunip.model.disk.support;

import java.util.List;

/**
 * 权限帮助类
 */
public class AuthHelperData {

    /****
     * 添加分享集合
     */
    private List<AuthHelper> authHelpers;

    public List<AuthHelper> getAuthHelpers() {
        return authHelpers;
    }

    public void setAuthHelpers(List<AuthHelper> authHelpers) {
        this.authHelpers = authHelpers;
    }
}
