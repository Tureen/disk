/*
 * 描述：文件加密状态枚举
 * 创建人：jian.xiong
 * 创建时间：2016-11-24
 */
package com.yunip.enums.disk;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件加密状态枚举
 */
public enum EncryptStatus {

    /**不加密**/
    UNENCRYPTED(0, "不加密"),

    /**待加密**/
    WAITFORENCRYPTION(1, "待加密"),
    
    /**已加密**/
    ENCRYPTED(2, "已加密");
    
    private EncryptStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    private Integer status;

    private String desc;
    
    public static List<EncryptStatus> getEncryptStatusList(){
        List<EncryptStatus> list = new ArrayList<EncryptStatus>();
        for(EncryptStatus bean : EncryptStatus.values()){
            list.add(bean);
        }
        return list;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}