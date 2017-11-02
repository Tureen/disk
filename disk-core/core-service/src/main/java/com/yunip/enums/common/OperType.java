/*
 * 描述：〈检验状态的枚举〉
 * 创建人：can.du
 * 创建时间：2016-4-20
 */
package com.yunip.enums.common;

/**
 * 状态的枚举
 */
public enum OperType {

    /**复制**/
    COPY(1, "复制"),

    /**移动**/
    MOVE(2, "移动");
    
    private OperType(Integer type, String desc) {
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
