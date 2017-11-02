/*
 * 描述：文件加密服务
 * 创建人：jian.xiong
 * 创建时间：2016-11-23
 */
package com.yunip.web.common;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunip.constant.SystemContant;
import com.yunip.model.disk.support.FileEncrypt;
import com.yunip.task.FileEncryptEntry;

/**
 * 文件加密服务
 */
public class FileEncryptService extends Thread{
    public static Logger logger = Logger.getLogger(FileEncryptService.class);
    
    /**
     * 当前正在执行加密操作的文件
     */
    public static List<String> CURRENT_EXECUTION_ENCRYPTION_QUEUE = new LinkedList<String>();
    
    /**
     * 同时进行加密操作的最大线程数
     */
    private static final int maxOperationCount = 3;
    
    int count = 0;
    
    @SuppressWarnings("static-access")
    @Override
    public void run() {
        while(true){
            if(!SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.isEmpty()){
                if(CURRENT_EXECUTION_ENCRYPTION_QUEUE.size() < maxOperationCount){
                    FileEncryptEntry entry = new FileEncryptEntry();
                    FileEncrypt fileEncrypt = SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.poll();
                    entry.setFileId(fileEncrypt.getFileId());
                    entry.setType(fileEncrypt.getType());
                    entry.start();
                }
                try {
                    this.currentThread().sleep(10);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{//等待加密文件队列池中为空时线程进入等待状态
                logger.info("等待加密文件队列池中为空，线程进入等待状态！");
                pleaseWait();
            }
        }
    }
    
    /**
     * 使线程处于等待状态
     */
    public boolean pleaseWait() {  
        synchronized (SystemContant.ENCRYPTION_FILE_WAIT) {  
            if (SystemContant.ENCRYPTION_FILE_WAIT.get() == true) {  
                return false;  
            }  
            SystemContant.ENCRYPTION_FILE_WAIT.set(true);  
            try {  
                SystemContant.ENCRYPTION_FILE_WAIT.wait();  
            } catch (InterruptedException e) {  
                logger.error("挂起等待文件加密线程失败：" + e.getMessage());
            }  
            return true;  
        }  
    }
    
    /***
     * 唤醒线程
     */
    public static boolean pleaseNotify() {  
        synchronized (SystemContant.ENCRYPTION_FILE_WAIT) {  
            if (SystemContant.ENCRYPTION_FILE_WAIT.get() == false) {  
                return true;  
            }  
            SystemContant.ENCRYPTION_FILE_WAIT.set(false);  
            try {  
                SystemContant.ENCRYPTION_FILE_WAIT.notify(); 
            } catch (Exception e) {  
                logger.error("唤醒文件加密线程失败：" + e.getMessage());
            }  
            return true;  
        }  
    }
}
