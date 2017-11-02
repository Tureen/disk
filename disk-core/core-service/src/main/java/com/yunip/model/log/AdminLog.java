/*
 * 描述：后台管理员or前台员工日志信息
 * 创建人：ming.zhu
 * 创建时间：2016-6-27
 */
package com.yunip.model.log;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("TAdminLog")
public class AdminLog implements Serializable {
    /** 主键 **/
    private Integer id;

    /** 行为类型  **/
    private Integer actionType;

    /** 操作内容 **/
    private String operContent;

    /** 操作人 **/
    private String operAdmin;

    /** 操作人ID **/
    private Integer adminId;

    /** 操作人IP **/
    private String operIp;

    /** 操作时间 **/
    private Date operTime;

    /** 是否为管理员(0.管理员,1.普通员工) **/
    private Integer isAdmin;
    
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

}