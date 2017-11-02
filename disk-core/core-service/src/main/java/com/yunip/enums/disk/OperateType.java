/*
 * 描述：〈操作行为〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-28
 */
package com.yunip.enums.disk;

public enum OperateType {

    Move(0, "移动"),

    Copy(1, "复制");

    private int    type;

    private String desc;

    private OperateType(int type, String desc) {
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
