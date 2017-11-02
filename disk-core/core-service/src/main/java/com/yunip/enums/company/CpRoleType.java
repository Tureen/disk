package com.yunip.enums.company;

/**
 * 员工角色类型枚举
 */
public enum CpRoleType {

    DEFAULTROLE(1, "默认角色包"),
    EXCLUSIVEROLE(2, "个人专属角色");
    
    private int    type;

    private String desc;

    private CpRoleType(int type, String desc) {
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
