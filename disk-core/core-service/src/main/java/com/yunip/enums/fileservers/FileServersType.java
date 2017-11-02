package com.yunip.enums.fileservers;

public enum FileServersType {
    
    /**上传文件操作**/
    UPLOAD(1,"上传"),
    
    /**下载文件操作**/
    DOWNLOAD(2,"下载"),
    
    /**在线编辑文件操作**/
    ONLINEEDIT(3,"在线编辑"),
    
    /**预览文件操作**/
    PREVIEWFILE(4,"预览");

    private FileServersType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int    type;

    private String desc;

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
