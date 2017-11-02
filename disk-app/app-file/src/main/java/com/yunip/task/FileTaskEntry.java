/*
 * 描述：〈文件处理程序入口〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.task;

import org.apache.log4j.Logger;

import com.yunip.constant.SystemContant;
import com.yunip.model.disk.File;
import com.yunip.model.disk.support.QueueTask;
import com.yunip.service.IFileService;
import com.yunip.util.SpringContextUtil;

/**
 * 文件处理程序入口
 */
public class FileTaskEntry extends Thread {

    Logger               logger      = Logger.getLogger(this.getClass());

    private IFileService fileService = SpringContextUtil.getBean("iFileService");

    /**队列对象**/
    private QueueTask    queueTask;

    @Override
    public void run() {
         if(queueTask != null){
             if(queueTask.getType() == QueueTask.DES_TYPE){
                 //解压
                 desFile(queueTask);
             } else if(queueTask.getType() == QueueTask.DOWN_TYPE){
                 //下载
             }
         }
    }

    /***
     * 解压文件
     * @param queueTask  消息队列
     * void 
     * @exception
     */
    private void desFile(QueueTask queueTask) {
        logger.info("解压任务：" + queueTask.getTaskName() + " 执行人:" + queueTask.getEmployee().getId());
        //加入处理队列中
        SystemContant.TASKLIST.add(queueTask.getTaskName());
        //开始进行解析
        File file = (File) queueTask.getObject();
        try {
            fileService.decompressFile(file, queueTask.getEmployee(), queueTask.getTaskName());
        } catch (Exception e) {
            SystemContant.TASKLIST.remove(queueTask.getTaskName());
            logger.info("解压失败任务：" + queueTask.getTaskName() + " 执行人:" + queueTask.getEmployee().getId());
        }
       
    }

    private void downFile(QueueTask queueTask) {

    }

    public QueueTask getQueueTask() {
        return queueTask;
    }

    public void setQueueTask(QueueTask queueTask) {
        this.queueTask = queueTask;
    }

}
