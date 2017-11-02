package com.yunip.enums;

public enum LoginQrcodeStatus {

    UNLOGIN(0,"未登录"),
    
    DOLOGIN(1,"正在登录"),
    
    LOGIN(2,"已登录"),
    
    CANCELLOGIN(3,"取消登录");
    
    private int status;
    
    private String desc;
    
    private LoginQrcodeStatus(int status, String desc) {
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
