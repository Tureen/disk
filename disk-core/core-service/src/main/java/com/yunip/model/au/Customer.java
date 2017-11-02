package com.yunip.model.au;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("TCustomer")
public class Customer implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 客户名称 **/
    private String            customerName;

    /** 联系人地址 **/
    private String            customerAddress;

    /** 联系人 **/
    private String            contacts;

    /** 联系人电话 **/
    private String            contactsMobile;

    /** 联系人邮箱 **/
    private String            contactsEmail;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    /************************ 辅助字段 **************************/

    /** 授权码 **/
    private String            licenseCode;

    /**授权时间(小时)**/
    private Integer           licenseHour;

    /** 客户端生成编码 **/
    private String            clientCode;

    /**注册员工数量秘钥**/
    private String            registerKey;

    /**允许注册员工数**/
    private Integer           registerNum;

    /**重新开通授权时长标识**/
    private Integer           licenseHourType;

    /**重新开通允许注册员工数标识**/
    private Integer           registerKeyType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsMobile() {
        return contactsMobile;
    }

    public void setContactsMobile(String contactsMobile) {
        this.contactsMobile = contactsMobile;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
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

    public Integer getLicenseHourType() {
        return licenseHourType;
    }

    public void setLicenseHourType(Integer licenseHourType) {
        this.licenseHourType = licenseHourType;
    }

    public Integer getRegisterKeyType() {
        return registerKeyType;
    }

    public void setRegisterKeyType(Integer registerKeyType) {
        this.registerKeyType = registerKeyType;
    }

}
