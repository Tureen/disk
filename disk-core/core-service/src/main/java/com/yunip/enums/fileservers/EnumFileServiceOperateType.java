/*
 * 描述：与文件服务器交互类型枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-5-27
 */
package com.yunip.enums.fileservers;

/**
 * 与文件服务器交互类型枚举类
 */
public enum EnumFileServiceOperateType {
    /**
     * 删除文件
     */
    DELETE_FILE("deleteFile", "删除文件", "/diskApi/deleteFile"),

    /**
     * 下载文件
     */
    DOWNLOAD_FILE("downloadFile", "下载文件", "/diskApi/downloadFile"),
    
    /**
     * 提取码下载文件
     */
    TAKECODE_DOWNLOAD_FILE("takeCodeDownloadFile", "提取码下载文件", "/diskApi/takeCodeDownloadFile"),
    
    /**
     * 回收站下载文件
     */
    RECOVERY_DOWNLOAD_FILE("recoveryDownloadFile", "回收站下载文件", "/diskApi/recoveryDownloadFile"),
    
    /**
     * 下载协助文件
     */
    TEAMWORK_DOWNLOAD_FILE("teamworkDownloadFile", "下载协助文件", "/diskApi/teamworkDownloadFile"),
    
    /**
     * 预览文件
     */
    PREVIEW_FILE("previewFile", "预览文件", "/diskApi/previewFile"),
    
    /**
     * 提取码预览文件
     */
    TAKECODE_PREVIEW_FILE("takeCodePreviewFile", "提取码预览文件", "/diskApi/takeCodePreviewFile"),
    
    /**
     * 预览协作文件
     */
    PREVIEW_TEAMWORK_FILE("previewTeamworkFile", "提取码预览文件", "/diskApi/previewTeamworkFile"),
    
    /**
     * 在线编辑文件
     */
    ONLINE_EDIT_FILE("onlineEditFile", "在线编辑文件", "/diskApi/onlineEditFile"),
    
    /**
     * 批量读取文件
     */
    BATCH_READER_FILE("batchReaderFile", "批量读取文件", "/diskApi/batchReaderFile"),
    
    /**
     * 批量读取协作文件
     */
    BATCH_READER_TEAMWORK_FILE("batchReaderTeamworkFile", "批量读取协作文件", "/diskApi/batchReaderTeamworkFile");
    
    /**
     * 接口ID
     */
    private String cmdId;
    
    /**
     * 接口名称
     */
    private String cmdName;
    
    /**
     * 接口地址
     */
    private String urlAddress;
    
    private EnumFileServiceOperateType(String cmdId, String cmdName, String urlAddress) {
        this.cmdId = cmdId;
        this.cmdName = cmdName;
        this.urlAddress = urlAddress;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }
}
