package com.yunip.enums.teamwork;

public enum MsgType {

    /**
     * 上传
     */
    UPLOAD(1, "上传"),
    
    /**
     * 删除
     */
    DELETE(2, "删除"),
    
    /**
     * 下载
     */
    DOWNLOAD(3, "下载"),
    
    /**
     * 新建文件夹
     */
    CREATE_FOLDER(4, "新建文件夹"),
    
    /**
     * 导出
     */
    EXPORT(5, "导出"),
    
    /**
     * 留言
     */
    LEAVE_MESSAGE(6, "留言"),
    
    /**
     * 重命名
     */
    RENAME(7, "重命名"),
    
    /**
     * 创建协作
     */
    CREATE_TEAMWORK(8, "创建协作"),
    
    /**
     * 添加协作成员
     */
    ADD_TEAMWORK_EMPLOYEE(9, "添加协作成员"),
    
    /**
     * 成员主动退出
     */
    QUIT_TEAMWORK_EMPLOYEE(10, "成员主动退出"),
    
    /**
     * 修改协作基础信息
     */
    EDIT_TEAMWORK(11, "修改协作基础信息"),
    
    /**
     * 移除协作成员
     */
    DELETE_TEAMWORK_EMPLOYEE(12, "移除协作成员"),;
    
    private int type;
    
    private String desc;

    private MsgType(int type, String desc) {
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
