/*
 * 描述：上传空间类型枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-5-27
 */
package com.yunip.enums.common;

/**
 * 上传空间类型枚举类
 */
public enum EnumSpaceType {
    /**
     * 私人空间(个人空间)
     */
    PRIVATE_SPACE("私人空间", "private"),

    /**
     * 公共空间
     */
    PUBLIC_SPACE("公共空间", "public"),
    
    /**
     * 协作空间
     */
    TEAMWORK_SPACE("协作空间", "teamwork");
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 代码
     */
    private String code;
    
    
    private EnumSpaceType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}