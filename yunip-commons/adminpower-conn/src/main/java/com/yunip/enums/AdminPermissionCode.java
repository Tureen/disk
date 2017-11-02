package com.yunip.enums;

public enum AdminPermissionCode {
    
    NEWFOLDER(65,"新建文件夹"),
    
    UPLOAD(66,"上传"),
    
    DOWNLOAD(67,"下载"),
    
    MOVE(68,"移动"),
    
    COPY(69,"复制"),
    
    DELETE(70,"删除"),
    
    RENAME(71,"重命名"),
    
    TAKECODE(72,"提取码"),
    
    SIGN(73,"标签"),
    
    SHARE(74,"分享"),
    
    EDIT(75,"编辑");
    
    private int    code;

    private String desc;

    private AdminPermissionCode(int code, String desc) {
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
