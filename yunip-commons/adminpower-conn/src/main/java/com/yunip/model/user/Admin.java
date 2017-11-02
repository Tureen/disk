package com.yunip.model.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.model.authority.Permission;
import com.yunip.model.authority.Role;

/***
 * 后台管理员登录信息
 * 一句话功能描述
 */
@Alias("TAdmin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键**/
    private Integer           id;

    /**手机**/
    private String            mobile;

    /**登录密码**/
    private String            accountPwd;

    /**有效状态（启用:1,冻结:0）**/
    private Integer           validStatus;

    /**登录表示*/
    private String            token;

    /**设备表示**/
    private String            deviceCode;

    /**ad域帐号**/
    private String            adName;

    /**0管理员 1普通员工**/
    private Integer           isAdmin;

    /**登录次数**/
    private Integer           loginTimes;

    /**最后登录时间**/
    private Date              lastTime;
    
    /**PC端登陆标识**/
    private String pcToken;

    /************************************辅助字段*************************************/
    /**权限编码的列表**/
    AdminAuthority            adminAuthority;

    /**角色集合**/
    List<Role>                roles;

    /**员工姓名**/
    String                    employeeName;

    /***权限对象列表***/
    private List<Permission>  permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAccountPwd() {
        return accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd == null ? null : accountPwd.trim();
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public AdminAuthority getAdminAuthority() {
        return adminAuthority;
    }

    public void setAdminAuthority(AdminAuthority adminAuthority) {
        this.adminAuthority = adminAuthority;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getPcToken() {
        return pcToken;
    }

    public void setPcToken(String pcToken) {
        this.pcToken = pcToken;
    }

}
