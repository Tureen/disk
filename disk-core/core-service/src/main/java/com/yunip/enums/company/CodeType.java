package com.yunip.enums.company;

/**
 * @author ming.zhu
 * 序列号类型枚举
 */
public enum CodeType {

    /**部门**/
    DEPT(1, "部门"),

    /**部门版本**/
    DVERSION(2, "部门版本"),

    /**员工版本**/
    EVERSION(3, "员工版本"),

    /**部门版本**/
    SYSVERSION(4, "系统配置版本");

    private int    type;

    private String name;

    private CodeType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
