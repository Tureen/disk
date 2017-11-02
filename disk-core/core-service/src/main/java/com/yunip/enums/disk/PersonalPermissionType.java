package com.yunip.enums.disk;

public enum PersonalPermissionType {

    CREATEFOLDER(65,"新建文件夹"),
    
    UPLOAD(66,"上传"),
    
    DOWNLOAD(67,"下载"),
    
    MOVE(68,"移动"),
    
    COPY(69,"复制"),
    
    DEL(70,"删除"),
    
    RENAME(71,"重命名"),
    
    TAKECODE(72,"提取码"),
    
    SIGN(73,"标签"),
    
    SHARE(74,"分享"),
    
    EDIT(75,"编辑");
    
    private int    type;

    private String desc;

    private PersonalPermissionType(int type, String desc) {
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
