package com.yunip.model.au;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("TLicenseLog")
public class LicenseLog implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 1L;

    /**主键**/
    private Integer           id;

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

    /**操作时间**/
    private Date              operTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

}
