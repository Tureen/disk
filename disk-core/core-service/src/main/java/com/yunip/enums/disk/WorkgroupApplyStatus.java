package com.yunip.enums.disk;

public enum WorkgroupApplyStatus {

    PendingAudit(0,"待审核","#ffa500"),
    AuditThrough(1,"审核通过","#008000"),
    AuditFailure(2,"审核不通过","#ff0000");
    
    private int status;
    private String desc;
    private String colorStyle;
    
    private WorkgroupApplyStatus(int status, String desc, String colorStyle) {
        this.status = status;
        this.desc = desc;
        this.colorStyle = colorStyle;
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
    public String getColorStyle() {
        return colorStyle;
    }
    public void setColorStyle(String colorStyle) {
        this.colorStyle = colorStyle;
    }
    
    
    
}
