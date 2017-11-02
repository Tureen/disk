/*
 * 描述：〈对象类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 对象类型
 */
public enum ShareType {

    DEPTMENT(1,"部门"),
    
    EMPLOYEE(2,"员工"),
    
    ALLCOM(3,"全公司"),
    
    WORKGROUP(4,"工作组");
    
    private int type;
    
    private String desc;
    
    private ShareType(int type, String desc) {
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
