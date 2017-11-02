/*
 * 描述：〈检验状态的枚举〉
 * 创建人：can.du
 * 创建时间：2016-4-20
 */
package com.yunip.enums.disk;

/**
 * 状态的枚举
 */
public enum ValidStatus {

    /**正常**/
    NOMAL(1, "正常"),

    /**删除**/
    DELETE(2, "删除");
    
    private ValidStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    private Integer status;

    private String desc;

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