/*
 * 描述：文件加密
 * 创建人：jian.xiong
 * 创建时间：2016-11-23
 */
package com.yunip.task;

import org.apache.log4j.Logger;

import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.util.SpringContextUtil;
import com.yunip.web.common.FileEncryptService;


/**
 * 文件加密
 */
public class FileEncryptEntry extends Thread{
    public static Logger logger = Logger.getLogger(FileEncryptEntry.class);
    private IFileEncryptDecryptService fileEncryptDecryptService = SpringContextUtil.getBean("iFileEncryptDecryptService");
    
    /**
     * 文件ID
     */
    private Integer fileId;
    
    /**
     * 文件or协作文件 0：文件  1：协作文件
     */
    private Integer type;
    
    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public void run() {
        if(fileId != null){
            FileEncryptService.CURRENT_EXECUTION_ENCRYPTION_QUEUE.add(fileId + "|" + type);
            if(type == 0){
                logger.info("当前加密文件ID：" + fileId + "，当前操作所在队列位置：" + FileEncryptService.CURRENT_EXECUTION_ENCRYPTION_QUEUE.size());
            }else{
                logger.info("当前加密协作文件ID：" + fileId + "，当前操作所在队列位置：" + FileEncryptService.CURRENT_EXECUTION_ENCRYPTION_QUEUE.size());
            }
            fileEncryptDecryptService.fileEncrypt(fileId, type);
            FileEncryptService.CURRENT_EXECUTION_ENCRYPTION_QUEUE.remove(fileId + "|" + type);
        }
    }
}
