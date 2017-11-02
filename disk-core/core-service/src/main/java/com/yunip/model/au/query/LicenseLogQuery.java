package com.yunip.model.au.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.au.LicenseLog;
import com.yunip.utils.page.PageQuery;

@Alias("TLicenseLogQuery")
public class LicenseLogQuery extends PageQuery<LicenseLog> implements
        Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 7199443737406650850L;

    /**客户端id*/
    private Integer           customerId;

    /**行为类型**/
    private Integer           actionType;

    /**操作内容**/
    private String            operContent;

    /**操作人**/
    private String            operAdmin;

    /**操作人ID**/
    private Integer           adminId;

    /**操作人IP**/
    private String            operIp;

    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getOperContent() {
        return operContent;
    }

    public void setOperContent(String operContent) {
        this.operContent = operContent;
    }

    public String getOperAdmin() {
        return operAdmin;
    }

    public void setOperAdmin(String operAdmin) {
        this.operAdmin = operAdmin;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
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

}
