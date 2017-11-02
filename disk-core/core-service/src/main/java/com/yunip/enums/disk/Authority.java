/*
 * 描述：〈权限枚举〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 权限枚举
 */
public enum Authority {

    /**创建**/
    CREATE(1, "创建 "),
    
    /**上传**/
    UPLOAD(2, "上传"),
    
    /**修改**/
    UPDATE(3, "修改"),
    
    /**移动**/
    MOVE(4, "移动"),
    
    /**下载**/
    DOWN(5, "下载"),
    
    /**复制**/
    COPY(6, "复制"),
    
    /**预览**/
    READ(7, "预览");

    private int    code;

    private String desc;

    private Authority(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
