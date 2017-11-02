/*
 * 描述：〈文件类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 文件类型
 */
public enum FileType {

    /***图片***/
    PIC(1, "图片", ResourceType.PIC, "jpg,jpeg,png,bmp"),

    /***压缩包***/
    ZIP(2, "压缩包", ResourceType.OTHER, "zip,z,tgz,tar,gtar,gz,rar"),

    /***文本***/
    TXT(3, "文本", ResourceType.FILE, "c,cpp,conf,h,log,prop,rc,sh,txt,xml"),

    /***office word***/
    WORD(4, "office word", ResourceType.FILE, "doc,docx"),

    /***office excel***/
    EXCEL(5, "office excel", ResourceType.FILE, "xls,xlsx"),

    /***office ppt***/
    PPT(6, "office ppt", ResourceType.FILE, "ppt,pps,pptx,ppsx"),

    /***wps文字***/
    WPSWZ(7, "wps文字", ResourceType.FILE, "wps,wpt,doc,dot,rtf"),

    /***wps演示***/
    WPSYS(8, "wps演示", ResourceType.FILE, "dps,dpt,ppt,pot,pps"),

    /***wps表格***/
    WPSBG(9, "wps表格", ResourceType.FILE, "et,ett,xls,xlt"),

    /***邮件***/
    EMAIL(10, "邮件", ResourceType.FILE, "msg,oft"),

    /***android安装文件***/
    APK(11, "android安装文件", ResourceType.OTHER, "apk"),

    /***ios安装文件***/
    IOS(12, "ios安装文件", ResourceType.OTHER, "ipa,deb,pxl"),

    /***动态图***/
    GIF(13, "动态图", ResourceType.PIC, "gif"),

    VIDEO(14, "视频文件", ResourceType.VIDEO,
            "avi,rmvb,rm,asf,divx,mpg,mpeg,mpe,wmv,mp4,mkv,vob"),
            
    UNKOWN(15, "未知类型", ResourceType.OTHER, ""),
    
    PDF(16, "PDF", ResourceType.FILE, "pdf");

    private int          type;

    private String       desc;

    private ResourceType resourceType;

    private String       fileType;

    /**文件结尾查找文件类型(类型txt,avi,请预先做好控制判断)**/
    public static FileType getFileType(String fileName) {
        String endFileName = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
        for (FileType fileType : FileType.values()) {
            String[] fileTypes = fileType.getFileType().split(",");
            for(String fileTypeStr : fileTypes){
                if (fileTypeStr.equalsIgnoreCase(endFileName)) {
                    return fileType;
                }
            }
        }
        return UNKOWN;
    }
    
    public static void main(String[] args) {
        FileType type = getFileType("aa.jpeg");
        System.out.println(type.desc);
    }

    private FileType(int type, String desc, ResourceType resourceType,
            String fileType) {
        this.type = type;
        this.desc = desc;
        this.resourceType = resourceType;
        this.fileType = fileType;
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

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
