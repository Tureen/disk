package com.yunip.enums;

public enum ScanLoginQrcodeStatus {

    UNLOGIN(0,"未登录未过期"),
    
    INVALIDUNLOGIN(1,"未登录已过期"),
    
    LOGINING(2,"正在登录未过期"),
    
    INVALIDLOGININING(3,"正在登录已过期"),
    
    COMPLETELOGIN(4,"完成登录");
    
    private int status;
    
    private String desc;
    
    private ScanLoginQrcodeStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
}
