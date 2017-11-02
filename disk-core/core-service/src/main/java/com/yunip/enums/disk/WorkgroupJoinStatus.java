package com.yunip.enums.disk;

/**
 * 员工对工作组的加入状态
 */
public enum WorkgroupJoinStatus {

    /**
     * 我已加入工作组
     */
    WYJR(1, "我已加入"),
    
    /**
     * 我未加入工作组
     */
    WWJR(0, "我未加入");
    
    private int status;
    private String desc;
    
    private WorkgroupJoinStatus(int status, String desc) {
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
