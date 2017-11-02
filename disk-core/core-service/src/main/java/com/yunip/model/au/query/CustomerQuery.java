package com.yunip.model.au.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.au.Customer;
import com.yunip.utils.page.PageQuery;

@Alias("TCustomerQuery")
public class CustomerQuery extends PageQuery<Customer> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3945820210135467461L;

    /**客户名称**/
    private String            customerName;

    /**联系人**/
    private String            contacts;

    /**联系人电话**/
    private String            contactsMobile;

    /**授权码**/
    private String            licenseCode;
    
    /**注册员工数量秘钥**/
    private String            registerKey;

    /**客户端生成编码**/
    private String            clientCode;

    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRegisterKey() {
        return registerKey;
    }

    public void setRegisterKey(String registerKey) {
        this.registerKey = registerKey;
    }

}
