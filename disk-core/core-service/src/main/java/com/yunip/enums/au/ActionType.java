package com.yunip.enums.au;

public enum ActionType {

    LICENSETIME(1,"修改授权时间"),
    
    REGISTERNUM(2,"修改使用员工数"),
    
    TIMEANDNUM(3,"修改授权时间及使用员工数"),
    
    BASIC(4,"修改基本信息");
    
    private int type;
    
    private String desc;
    
    private ActionType(int type, String desc) {
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
