/*
 * 描述：〈管理员查询类〉
 * 创建人：can.du
 * 创建时间：2016-4-20
 */
package com.yunip.model.user.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.user.Admin;
import com.yunip.utils.page.PageQuery;

/**
 * 管理员查询类
 */
@Alias("TAdminQuery")
public class AdminQuery extends PageQuery<Admin> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户id **/
    private Integer           id;

    /**手机号码**/
    private String            mobile;

    /**登录密码**/
    private String            password;

    /**使用状态*/
    private Integer           validStatus;

    /**是否记住帐号密码**/
    private boolean           bool;

    /***终端登录标识**/
    private String            token;
    
    /**PC端登陆标识**/
    private String pcToken;

    /**ad域帐号**/
    private String            adName;

    /**0管理员 1普通员工**/
    private Integer           isAdmin;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public String getPcToken() {
        return pcToken;
    }

    public void setPcToken(String pcToken) {
        this.pcToken = pcToken;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }
}
