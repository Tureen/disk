package com.yunip.enums.admin;

/**
 * 注册用户类型枚举
 */
public enum AdminType {

    Admin(0, "管理员"),
    Employee(1, "员工");
    
    private int    type;

    private String desc;

    private AdminType(int type, String desc) {
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
