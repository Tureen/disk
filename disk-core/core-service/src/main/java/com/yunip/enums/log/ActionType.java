/*
 * 描述：〈 操作类型(员工行为日志)〉
 * 创建人：can.du
 * 创建时间：2016-6-27
 */
package com.yunip.enums.log;

/**
 * 操作类型(员工行为日志)
 */
public enum ActionType {

    /**登录**/
    LOGIN(1, "登录"),

    /**上传**/
    UPLOAD(2, "上传"),

    /**下载**/
    DOWNLOAD(3, "下载"),

    /**新建文件夹**/
    CREATE(4, "新建文件夹"),

    /**重命名**/
    RENAME(5, "重命名"),

    /**删除**/
    DELETE(6, "删除"),

    /**复制**/
    COPY(7, "复制"),

    /**移动**/
    MOVE(8, "移动"),

    /**分享**/
    SHARE(9, "分享"),

    /**移除分享**/
    RESHARE(10, "移除分享"),

    /**创建提取码**/
    CTAKECODE(11, "创建提取码"),

    /**移除提取码**/
    RETAKECODE(12, "移除提取码"),

    /**在线编辑**/
    ONLINEEDIT(13, "在线编辑");

    /***
     * 类型
     */
    private int    type;

    /***
     * 描述
     */
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
