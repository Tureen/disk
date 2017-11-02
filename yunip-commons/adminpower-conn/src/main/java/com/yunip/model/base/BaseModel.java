/*
 * 描述：〈基本对象〉
 * 创建人：can.du
 * 创建时间：2016-4-21
 */
package com.yunip.model.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述
 */
public class BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 创建人
     */
    private String            createAdmin;

    /**
     * 创建时间
     */
    private Date              createTime;

    /**
     * 更新人
     */
    private String            updateAdmin;

    /**
     * 更新时间
     */
    private Date              updateTime;

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
}
