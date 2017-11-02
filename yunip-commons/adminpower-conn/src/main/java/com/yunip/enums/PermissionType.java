/*
 * 描述：〈权限类型的枚举〉
 * 创建人：can.du
 * 创建时间：2016-4-20
 */
package com.yunip.enums;

/**
 * 权限类型的枚举
 */
public enum PermissionType {

    /**导航**/
    NAVIGATION(1, "一级菜单"),

    /**导航页面**/
    PAGE(2, "二级菜单"),

    /**功能点**/
    FUNCTION(3, "功能点");
    
    private PermissionType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private Integer type;

    private String desc;

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
