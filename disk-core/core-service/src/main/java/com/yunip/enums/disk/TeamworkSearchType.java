package com.yunip.enums.disk;

/**
 * 协作查询类型
 */
public enum TeamworkSearchType {
    
    /**全部协作**/
    ALL_TEAMWORK(1, "全部协作"),
    
    /**我是管理员**/
    TEAMWORK_ADMIN(2, "我是管理员"),
    
    /**我是成员**/
    TEAMWORK_MEMBER(3, "我是成员");

    private int type;
    
    private String desc;

    private TeamworkSearchType(int type, String desc) {
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
