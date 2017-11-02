package com.yunip.model.config.support;

import java.io.Serializable;
import java.util.Date;

public class LoginQrcode implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -2471529289912035666L;

    /** 二维码编号 **/
    private String            code;

    /** 成功登录时，登录者token **/
    private String            token;

    /** 登录者IP **/
    private String            loginIp;

    /** 状态 0：未登录  1：正在登录  2：已登录 **/
    private int               status;

    /** 失效性 **/
    private int               effective;

    /** 创建时间 **/
    private Date              createTime;

    /** 失效时间 **/
    private Date              effectiveTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

}
