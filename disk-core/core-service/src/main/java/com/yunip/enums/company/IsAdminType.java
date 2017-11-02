package com.yunip.enums.company;

public enum IsAdminType {
    
    GLY(0, "管理员"),
    
    YG(1, "员工");

    private IsAdminType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private Integer type;

    private String  desc;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
