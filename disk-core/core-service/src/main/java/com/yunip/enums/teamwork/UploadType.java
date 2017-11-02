package com.yunip.enums.teamwork;

public enum UploadType {

    /**
     * 新增
     */
    INSERT(0, "新增"),
    
    /**
     * 升级或覆盖
     */
    OVER_UPVSERSION(1, "升级或覆盖");
    
    private int type;
    
    private String desc;

    private UploadType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
}
