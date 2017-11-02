package com.yunip.enums;

public enum QrcodeEffectiveStatus {

    EFFECTIVE(0,"有效"),
    
    UNEFFECTIVE(1,"失效");
    
    private int status;
    
    private String desc;
    
    private QrcodeEffectiveStatus(int status, String desc) {
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
