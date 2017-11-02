package com.yunip.enums.teamwork;

public enum FileType {

    /**
     * 文件
     */
    IS_FILE(0, "文件"),
    
    /**
     * 协作文件
     */
    IS_TEAMWORK_FILE(1, "协作文件");
    
    private int type;
    
    private String desc;

    private FileType(int type, String desc) {
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
