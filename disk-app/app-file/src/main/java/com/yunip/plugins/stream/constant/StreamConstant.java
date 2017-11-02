/*
 * 描述：stream上传控件常量类
 * 创建人：jian.xiong
 * 创建时间：2016-7-19
 */
package com.yunip.plugins.stream.constant;

/**
 * stream上传控件常量类
 */
public class StreamConstant {
    public static final String FILE_NAME_FIELD = "name";
    public static final String FILE_SIZE_FIELD = "size";
    public static final String TOKEN_FIELD = "token";
    public static final String SERVER_FIELD = "server";
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    /** 当前目录（文件夹）ID **/
    public static final String FOLDER_ID = "folderId";
    /** 上传空间类型(个人空间/公共空间) **/
    public static final String SPACE_TYPE = "spaceType";
    
    public static final int STREAM_BUFFER_LENGTH = 10240;
    public static final String START_FIELD = "start";
    public static final String Z_FILE_ID = "zFileId";
    public static final String TEAMWORK_MESSGE_ID = "teamworkMessageId";
    public static final String CONTENT_RANGE_HEADER = "content-range";
    
    static final String FILE_FIELD = "file";
    /** when the has read to 10M, then flush it to the hard-disk. */
    public static final int FORMDATA_BUFFER_LENGTH = 1024 * 1024 * 10;
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 100;
    
    /** 操作人 **/
    public static String OPERATOR = "1";
}
