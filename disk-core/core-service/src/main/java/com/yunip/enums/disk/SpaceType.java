package com.yunip.enums.disk;

/**
 * 云盘功能栏目枚举
 */
public enum SpaceType {
    
    DISKSPACE("1","总览","/home/index"),
    
    PERSONALSPACE("2","个人空间","/personal/index"),
    
    COMSHARESPACE("3","公共空间","/comshare/index"),
    
    SHARESPACE("4","我的共享","/share/index"),
    
    BSHARESPACE("5","他人共享","/bshare/index"),
    
    SIGNSPACE("6","标签管理","/sign/index"),
    
    TAKECODESPACE("7","我的提取码","/takecode/index");

    private SpaceType(String type, String desc, String path) {
        this.type = type;
        this.desc = desc;
        this.path = path;
    }

    private String type;
    
    private String desc;
    
    private String path;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
    
}
