package com.yunip.model.au.query;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.model.au.LicenseCode;
import com.yunip.utils.page.PageQuery;

@Alias("TLicenseCodeQuery")
public class LicenseCodeQuery extends PageQuery<LicenseCode> implements
        Serializable {

    private static final long serialVersionUID = -8701409868094513131L;

    /** 客户端生成编码 **/
    private String            clientCode;

    /** 授权码 **/
    private String            licenseCode;

    /** 授权时间(小时) **/
    private Integer           licenseHour;

    /**注册员工数量秘钥**/
    private String            registerKey;

    /**允许注册员工数**/
    private Integer           registerNum;

    /** 客户端ID **/
    private Integer           customerId;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Integer getLicenseHour() {
        return licenseHour;
    }

    public void setLicenseHour(Integer licenseHour) {
        this.licenseHour = licenseHour;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(String createAdmin) {
        this.createAdmin = createAdmin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRegisterKey() {
        return registerKey;
    }

    public void setRegisterKey(String registerKey) {
        this.registerKey = registerKey;
    }

    public Integer getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

}
