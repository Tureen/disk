/*
 * 描述：常量类
 * 创建人：jian.xiong
 * 创建时间：2016-5-11
 */
package com.yunip.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 常量类
 */
public class Constant {
    /**session存储的key**/
    public final static String UPLOADUSER_IN_SESSION = "uploadUser";
    
    /**cookie存储的key**/
    public final static String ADMIN_IN_COOKIE = "admin";
    
    /** 文件存放根目录(C:\\file) */
    public static String ROOTPATH = "C:\\file";
    
    /** 上传目录 */
    public static final String UPLOAD_DIR = "upload";
    
    /** 索引目录 */
    public static final String INDEX_DIR = "index";
    
    /** 上传临时目录 */
    public static final String UPLOAD_TEMP_DIR = "temp";
    
    /** 文件预览临时目录 */
    //public static final String FILE_PREVIEW_DIR = ROOTPATH + File.separator + "preview";
    public static final String FILE_PREVIEW_DIR = "preview";
    
    /** 存放需要进行压缩下载的原文件临时目录 */
    public static final String ZIP_DOWN_TEMP_DIR = "download";
    
    /** 压缩下载生成压缩文件的文件名  */
    public static final String ZIP_DOWN_FILE_NAME = "download.zip";
    
    /** 文件备份目录 **/
    public static final String FILE_BACKUP_DIR = "backup";
    
    /** zip文件后缀名  */
    public static final String ZIP_SUFFIX = ".zip";
    
    /** pdf文件后缀名  */
    public static final String PDF_SUFFIX = ".pdf";
    
    /** swf文件后缀名 */
    public static final String SWF_SUFFIX = ".swf";
    
    /**文件以及字符编码*/
    public static final String encodeType = "utf-8";
    
    /** 允许预览的文件类型 **/
    public static final List<String> previewFileType = Arrays.asList(".c", ".cpp", ".conf", ".h", ".log", ".prop", ".rc", ".sh", ".txt", ".xml", ".sql", ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".wps", ".et", ".dps");
    
    /** 允许在线编辑的文件类型 **/
    public static final List<String> onlineEditFileType = Arrays.asList(".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".wps", ".et", ".dps", ".vsd");
    
    /** 允许以文本方式进行在线编辑的文件类型 **/
    public static final List<String> onlineEditTxtFileType = Arrays.asList(".c", ".cpp", ".conf", ".h", ".log", ".prop", ".rc", ".sh", ".txt", ".xml", ".sql");
    
    /** 允许生成缩略图的文件类型 **/
    public static final List<String> thumbImageType = Arrays.asList(".jpg", ".jpeg", ".png", ".bmp");
    
    /** 图片文件对应的缩略图前缀名 **/
    public static final String thumbImagePrefixName = "small-";
    
    /** 图片文件对应的预览图前缀名 **/
    public static final String previewImagePrefixName = "preview-";
    
    /** 图片文件预览时超出该大小时进行压缩，生成预览小图(单位：字节) **/
    public static final int imagePreviewMinCompressSize = 1024 * 100;
    
    /** 图片文件预览时，生成预览图的宽、高比例基数 **/
    public static final int imagePreviewPixelRadix = 400;
}
