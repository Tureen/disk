/*
 * 描述：〈队列处理〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.task;

import org.apache.log4j.Logger;

import com.yunip.service.IFileIndexService;
import com.yunip.util.SpringContextUtil;

/**
 * 一句话功能描述
 */
public class LuceneEntry extends Thread{
    
    public static Logger logger = Logger.getLogger(LuceneEntry.class);
    
    private IFileIndexService fileIndexService = SpringContextUtil.getBean("iFileIndexService");

    @Override
    public void run() {
        while(true){
            try {
                createIndex();
                /**一分钟执行一次***/
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void createIndex(){
        logger.info("检测索引记录信息.");
        try {
            fileIndexService.createFileIndex();
        } catch (Exception e) {
            logger.info("索引创建出现异常：" + e.getMessage());
        }
        
    }


}
